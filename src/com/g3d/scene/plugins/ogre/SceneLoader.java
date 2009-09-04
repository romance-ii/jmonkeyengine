package com.g3d.scene.plugins.ogre;

import com.g3d.asset.AssetInfo;
import com.g3d.asset.AssetLoader;
import com.g3d.asset.AssetManager;
import com.g3d.math.ColorRGBA;
import com.g3d.math.Quaternion;
import com.g3d.math.Vector3f;
import com.g3d.scene.Node;
import com.g3d.scene.Spatial;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.helpers.XMLReaderFactory;
import static com.g3d.util.xml.SAXUtil.*;

public class SceneLoader extends DefaultHandler implements AssetLoader {

    private static final Logger logger = Logger.getLogger(SceneLoader.class.getName());

    private String curElement;
    private String sceneName;
    private AssetManager assetManager;
    private Node root;
    private Node node;
    private Node entityNode;
    private int nodeIdx = 0;
    private static volatile int sceneIdx = 0;

    public SceneLoader(){
        super();
    }

    public static void main(String[] args) throws SAXException{
        AssetManager manager = new AssetManager(true);
        manager.loadModel("Cube.meshxml");
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    private Vector3f parseVector3(Attributes attribs) throws SAXException{
        float x = parseFloat(attribs.getValue("x"));
        float y = parseFloat(attribs.getValue("y"));
        float z = parseFloat(attribs.getValue("z"));
        return new Vector3f(x,y,z);
    }

    private ColorRGBA parseColor(Attributes attribs) throws SAXException{
        float r = parseFloat(attribs.getValue("r"));
        float g = parseFloat(attribs.getValue("g"));
        float b = parseFloat(attribs.getValue("b"));
        return new ColorRGBA(r, g, b, 1f);
    }

    private Quaternion parseQuat(Attributes attribs) throws SAXException{
        if (attribs.getValue("qx") != null){
            // defined as quaternion
            // qx, qy, qz, qw defined
            float x = parseFloat(attribs.getValue("qx"));
            float y = parseFloat(attribs.getValue("qy"));
            float z = parseFloat(attribs.getValue("qz"));
            float w = parseFloat(attribs.getValue("qw"));
            return new Quaternion(x,y,z,w);
        }else if (attribs.getValue("angle") != null){
            // defined as angle + axis
            float angle = parseFloat(attribs.getValue("angle"));
            float axisX = parseFloat(attribs.getValue("axisX"));
            float axisY = parseFloat(attribs.getValue("axisY"));
            float axisZ = parseFloat(attribs.getValue("axisZ"));
            Quaternion q = new Quaternion();
            q.fromAngleAxis(angle, new Vector3f(axisX, axisY, axisZ));
            return q;
        }else{
            float angleX = parseFloat(attribs.getValue("angleX"));
            float angleY = parseFloat(attribs.getValue("angleY"));
            float angleZ = parseFloat(attribs.getValue("angleZ"));
            Quaternion q = new Quaternion();
            q.fromAngles(angleX, angleY, angleZ);
            return q;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attribs) throws SAXException{
        if (qName.equals("scene")){
            assert curElement == null;
            curElement = "scene";
            String version = attribs.getValue("formatVersion");
            if (!version.equals("1.0.0"))
                logger.warning("Unrecognized version number in dotScene file: "+version);
        }else if (qName.equals("nodes")){
            assert root == null;
            curElement = "nodes";
            if (sceneName == null)
                root = new Node("OgreDotScene"+(++sceneIdx));
            else
                root = new Node(sceneName+"-scene_node");
            
            node = root;
        }else if (qName.equals("externals")){
            assert curElement.equals("scene");
        }else if (qName.equals("node")){
            assert curElement.equals("nodes") || curElement.equals("node");
            curElement = "node";
            String name = attribs.getValue("name");
            if (name == null)
                name = "OgreNode-" + (++nodeIdx);
            else
                name += "-node";

            Node newNode = new Node(name);
            if (node != null){
                node.attachChild(newNode);
            }
            node = newNode;
        }else if (qName.equals("entity")){
            assert curElement.equals("node");
            String name = attribs.getValue("name");
            if (name == null)
                name = "OgreEntity-" + (++nodeIdx);
            else
                name += "-entity";

            String meshFile = attribs.getValue("meshFile");
            if (meshFile == null)
                throw new SAXException("Required attribute 'meshFile' missing for 'entity' node");

            entityNode = new Node(name);
            Spatial ogreMesh = assetManager.loadModel(meshFile);
            entityNode.attachChild(ogreMesh);
            node.attachChild(entityNode);
            node = null;
        }else if (qName.equals("position")){
            node.setLocalTranslation(parseVector3(attribs));
        }else if (qName.equals("rotation")){
            node.setLocalRotation(parseQuat(attribs));
        }else if (qName.equals("scale")){
            node.setLocalScale(parseVector3(attribs));
        }
    }

    @Override
    public void endElement(String uri, String name, String qName) {
        if (qName.equals("node")){
            node = node.getParent();
        }else if (qName.equals("nodes")){
            node = null;
        }else if (qName.equals("entity")){
            node = entityNode.getParent();
            entityNode = null;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    public Object load(AssetInfo info) throws IOException {
        try{
            assetManager = info.getManager();
            sceneName = info.getKey().getName();
            String ext = info.getKey().getExtension();
            sceneName = sceneName.substring(0, sceneName.length() - ext.length() - 1);

            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            InputStreamReader r = new InputStreamReader(info.openStream());
            xr.parse(new InputSource(r));
            r.close();
            return root;
        }catch (SAXException ex){
            throw new IOException("Error while parsing Ogre3D mesh.xml", ex);
        }

    }

}
