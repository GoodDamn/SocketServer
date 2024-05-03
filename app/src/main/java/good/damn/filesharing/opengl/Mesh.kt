package good.damn.filesharing.opengl

import android.content.Context
import good.damn.filesharing.opengl.entities.Entity

class Mesh(
    obj: Object3D,
    program: Int
): Entity(
    obj.vertices,
    obj.indices,
    program
) {

}