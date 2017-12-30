package next;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class X_Fx3DShapeExample1 extends Application {
        public static void main(String[] args) {
                Application.launch(args);
        }

        @Override
        public void start(Stage stage) {
                // Create a Box
                Box box = new Box(500, 100, 10);
                box.setTranslateX(500);
                box.setTranslateY(750);
                box.setTranslateZ(0);
                box.setMaterial(new PhongMaterial(Color.WHEAT));

                //MeshView hex = new MeshView(new X_Hex());

                /*// Create a Sphere
                Sphere sphere = new Sphere(50);
                sphere.setTranslateX(300);
                sphere.setTranslateY(-5);
                sphere.setTranslateZ(400);

                // Create a Cylinder
                Cylinder cylinder = new Cylinder(40, 120);
                cylinder.setTranslateX(500);
                cylinder.setTranslateY(-25);
                cylinder.setTranslateZ(600);*/

                // Create a Light
                PointLight light = new PointLight();
                light.setTranslateX(450);
                light.setTranslateY(700);
                light.setTranslateZ(-200);
                light.setColor(Color.RED);

                // Create a Camera to view the 3D Shapes
                PerspectiveCamera camera = new PerspectiveCamera(false);
                camera.setTranslateX(0);
                camera.setTranslateY(0);
                camera.setTranslateZ(-1000);

                // Add the Shapes and the Light to the Group
                Group root = new Group(box, /*sphere, cylinder,*/ light);

                // Create a Scene with depth buffer enabled
                Scene scene = new Scene(root, 1000, 1000, true);
                // Add the Camera to the Scene
                scene.setCamera(camera);

                // Add the Scene to the Stage
                stage.setScene(scene);
                // Set the Title of the Stage
                stage.setTitle("An Example with Predefined 3D Shapes");
                // Display the Stage
                stage.show();
        }
}

