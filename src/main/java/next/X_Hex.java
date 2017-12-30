package next;

import javafx.scene.shape.TriangleMesh;

public class X_Hex extends TriangleMesh {
    private X_Hex(int radius, int height) {
        float[] points = {
                -5, 5, 0,
                -5, -5, 0,
                5, 5, 0,
                5, -5, 0
        };
        float[] texCoords = {
                0, 0,
                0, 1,
                1, 0,
                1, 1
        };
        int[] faces = {
                0, 0, 1, 1, 2, 2,
                2, 2, 3, 3, 1, 1
        };

        this.getPoints().addAll(points);
        this.getTexCoords().addAll(texCoords);
        this.getFaces().addAll(faces);
    }
}
