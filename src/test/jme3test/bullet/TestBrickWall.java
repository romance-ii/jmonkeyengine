package jme3test.bullet;

import com.jme3.app.SimpleBulletApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.nodes.PhysicsNode;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.texture.Texture;

/**
 *
 * @author double1984
 */
public class TestBrickWall extends SimpleBulletApplication {

    float bLength = 0.48f;
    float bWidth = 0.24f;
    float bHeight = 0.12f;
    Material mat;
    Material mat2;
    BasicShadowRenderer bsr;

    public static void main(String args[]) {
        TestBrickWall f = new TestBrickWall();
        f.start();
    }

    @Override
    public void simpleInitApp() {
        initMaterial();
        initWall();
        initFloor();
        initCrossHairs();
        this.cam.setLocation(new Vector3f(0, 6f, 6f));
        cam.lookAt(Vector3f.ZERO, new Vector3f(0, 1, 0));
        cam.setFrustumFar(15);
        inputManager.addMapping("shoot", new MouseButtonTrigger(0));
        inputManager.addListener(actionListener, "shoot");

        rootNode.setShadowMode(ShadowMode.Off);
        bsr = new BasicShadowRenderer(assetManager, 512);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
        //System.out.print(this.getPhysicsSpace().getAccuracy());
        this.getPhysicsSpace().setAccuracy(0.005f);
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                Sphere bullet = new Sphere(16, 16, 0.4f);
                Geometry bulletg = new Geometry("bullet", bullet);
                bulletg.setMaterial(mat2);
                PhysicsNode bulletNode = new PhysicsNode(bulletg, new SphereCollisionShape(0.2f), 1);
                bulletNode.setLocalTranslation(cam.getLocation());
                bulletNode.updateModelBound();
                bulletNode.updateGeometricState();
                bulletNode.setShadowMode(ShadowMode.CastAndRecieve);

                bulletNode.setLinearVelocity(cam.getDirection().mult(25));
                // bulletNode.setMass(1f);
                System.out.println(bulletNode.getMass());
                rootNode.attachChild(bulletNode);
                getPhysicsSpace().add(bulletNode);
            }
        }
    };

    public void initWall() {
        float startpt = bLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 4; i++) {
                Vector3f vt = new Vector3f(i * bLength * 2 + startpt, bHeight + height, 0);
                addBrick(vt);
            }
            startpt = -startpt;
            height += 2 * bHeight;
        }
    }

    public void initFloor() {
        Material fmat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/Pond/Pond.png");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        fmat.setTexture("floorTexture", tex);

        Box floorBox = new Box(Vector3f.ZERO, 10f, 0.1f, 5f);
        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(fmat);
        floor.setShadowMode(ShadowMode.Recieve);
        PhysicsNode floorNode = new PhysicsNode(floor, new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)), 0);
        floorNode.setLocalTranslation(0, -0.1f, 0);
        floorNode.updateGeometricState();
        floorNode.updateModelBound();
        this.rootNode.attachChild(floorNode);
        this.getPhysicsSpace().add(floorNode);
    }

    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg", true);
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        mat.setTexture("color map", tex);

        mat2 = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG", true);
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat2.setTexture("color map", tex2);
    }

    public void addBrick(Vector3f ori) {
        Box b = new Box(Vector3f.ZERO, bLength, bHeight, bWidth);
        Geometry reBoxg = new Geometry("brick", b);
        reBoxg.setMaterial(mat);
        // reBoxg.setShadowMode(ShadowMode.CastAndRecieve);
        PhysicsNode brickNode = new PhysicsNode(reBoxg, new BoxCollisionShape(new Vector3f(bLength, bHeight, bWidth)), 1);
        brickNode.setLocalTranslation(ori);
        brickNode.updateModelBound();
        brickNode.updateGeometricState();
        brickNode.setShadowMode(ShadowMode.CastAndRecieve);
        //^ brickNode.setFriction(1f);
        brickNode.setMass(1.5f);
        this.rootNode.attachChild(brickNode);
        this.getPhysicsSpace().add(brickNode);
    }

    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
}
