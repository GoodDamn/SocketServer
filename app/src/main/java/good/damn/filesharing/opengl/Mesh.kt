package good.damn.filesharing.opengl

import android.content.Context

class Mesh(
    context: Context,
    resourceId: Int
): Entity() {

    private val object3D: Object3D

    init {

        object3D = Object3D
            .createFromResources(
                resourceId,
                context
            )
    }

}