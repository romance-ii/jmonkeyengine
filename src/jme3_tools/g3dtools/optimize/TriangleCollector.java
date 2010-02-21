package g3dtools.optimize;

import com.g3d.scene.Geometry;
import com.g3d.scene.Mesh;
import com.g3d.scene.VertexBuffer;
import com.g3d.scene.VertexBuffer.Type;
import com.g3d.util.BufferUtils;
import com.g3d.util.IntMap;
import com.g3d.util.IntMap.Entry;
import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriangleCollector {

    private static final GeomTriComparator comparator = new GeomTriComparator();

    private static class GeomTriComparator implements Comparator<OCTTriangle> {
        public int compare(OCTTriangle a, OCTTriangle b) {
            if (a.getGeometryIndex() < b.getGeometryIndex()){
                return -1;
            }else if (a.getGeometryIndex() > b.getGeometryIndex()){
                return 1;
            }else{
                return 0;
            }
        }
    }

    private static class Range {
        
        private int start, length;

        public Range(int start, int length) {
            this.start = start;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

    }

    /**
     * Grabs all the triangles specified in <code>tris</code> from the input array
     * (using the indices OCTTriangle.getGeometryIndex() & OCTTriangle.getTriangleIndex())
     * then organizes them into output geometry.
     *
     * @param geoms
     * @param tris
     * @return
     */
    public static final List<Geometry> gatherTris(Geometry[] inGeoms, List<OCTTriangle> tris){
        Collections.sort(tris, comparator);
        HashMap<Integer, Range> ranges = new HashMap<Integer, Range>();

        for (int i = 0; i < tris.size(); i++){
            Range r = ranges.get(tris.get(i).getGeometryIndex());
            if (r != null){
                // incremenet length
                r.setLength(r.getLength()+1);
            }else{
                // set offset, length is 1
                ranges.put(tris.get(i).getGeometryIndex(), new Range(i, 1));
            }
        }
        
        List<Geometry> newGeoms = new ArrayList<Geometry>();
        int[] vertIndicies = new int[3];
        int[] newIndices = new int[3];
        boolean[] vertexCreated = new boolean[3];
        HashMap<Integer, Integer> indexCache = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Range> entry : ranges.entrySet()){
            int inGeomIndex = entry.getKey().intValue();
            int outOffset = entry.getValue().start;
            int outLength = entry.getValue().length;

            Geometry inGeom = inGeoms[inGeomIndex];
            Mesh in = inGeom.getMesh();
            Mesh out = new Mesh();

            int outElementCount = outLength * 3;
            ShortBuffer ib = BufferUtils.createShortBuffer(outElementCount);
            out.setBuffer(Type.Index, 3, ib);

            // generate output buffers based on input buffers
            IntMap<VertexBuffer> bufs = in.getBuffers();
            for (Entry<VertexBuffer> ent : bufs){
                VertexBuffer vb = ent.getValue();
                if (vb.getBufferType() == Type.Index)
                    continue;

                // NOTE: we are not actually sure
                // how many elements will be in this buffer.
                // It will be compacted later.
                Buffer b = VertexBuffer.createBuffer(vb.getFormat(), 
                                                     vb.getNumComponents(),
                                                     outElementCount);

                VertexBuffer outVb = new VertexBuffer(vb.getBufferType());
                outVb.setNormalized(vb.isNormalized());
                outVb.setupData(vb.getUsage(), vb.getNumComponents(), vb.getFormat(), b);
                out.setBuffer(outVb);
            }

            int currentVertex = 0;
            for (int i = outOffset; i < outOffset + outLength; i++){
                OCTTriangle t = tris.get(i);

                // find vertex indices for triangle t
                in.getTriangle(t.getTriangleIndex(), vertIndicies);

                // find indices in new buf
                Integer i0 = indexCache.get(vertIndicies[0]);
                Integer i1 = indexCache.get(vertIndicies[1]);
                Integer i2 = indexCache.get(vertIndicies[2]);

                // check which ones were not created
                // if not created in new IB, create them
                if (i0 == null){
                    vertexCreated[0] = true;
                    newIndices[0] = currentVertex++;
                    indexCache.put(vertIndicies[0], newIndices[0]);
                }else{
                    newIndices[0] = i0.intValue();
                    vertexCreated[0] = false;
                }
                if (i1 == null){
                    vertexCreated[1] = true;
                    newIndices[1] = currentVertex++;
                    indexCache.put(vertIndicies[1], newIndices[1]);
                }else{
                    newIndices[1] = i1.intValue();
                    vertexCreated[1] = false;
                }
                if (i2 == null){
                    vertexCreated[2] = true;
                    newIndices[2] = currentVertex++;
                    indexCache.put(vertIndicies[2], newIndices[2]);
                }else{
                    newIndices[2] = i2.intValue();
                    vertexCreated[2] = false;
                }

                // if any verticies were created for this triangle
                // copy them to the output mesh
                IntMap<VertexBuffer> inbufs = in.getBuffers();
                for (Entry<VertexBuffer> ent : inbufs){
                    VertexBuffer vb = ent.getValue();
                    if (vb.getBufferType() == Type.Index)
                        continue;
                    
                    VertexBuffer outVb = out.getBuffer(vb.getBufferType());
                    // copy verticies that were created for this triangle
                    for (int v = 0; v < 3; v++){
                        if (!vertexCreated[v])
                            continue;

                        // copy triangle's attribute from one
                        // buffer to another
                        vb.copyElement(vertIndicies[v], outVb, newIndices[v]);
                    }
                }

                // write the indices onto the output index buffer
                ib.put((short)newIndices[0])
                  .put((short)newIndices[1])
                  .put((short)newIndices[2]);
            }
            ib.clear();
            indexCache.clear();

            // since some verticies were cached, it means there's
            // extra data in some buffers
            IntMap<VertexBuffer> outbufs = out.getBuffers();
            for (Entry<VertexBuffer> ent : outbufs){
                VertexBuffer vb = ent.getValue();
                if (vb.getBufferType() == Type.Index)
                    continue;

                vb.compact(currentVertex);
            }

            out.updateBound();
            out.updateCounts();
            out.setStatic();
            out.setInterleaved();
            Geometry outGeom = new Geometry("Geom"+entry.getKey(), out);
            outGeom.setTransform(inGeom.getWorldTransform());
            outGeom.setMaterial(inGeom.getMaterial());
            outGeom.updateGeometricState();
            newGeoms.add(outGeom);
        }

        return newGeoms;
    }

}
