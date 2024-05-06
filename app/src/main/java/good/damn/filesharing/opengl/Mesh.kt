package good.damn.filesharing.opengl

import android.content.Context
import good.damn.filesharing.opengl.entities.Entity
import good.damn.filesharing.opengl.textures.Texture

class Mesh(
    obj: Object3D,
    texturePath: String,
    program: Int
): Entity(
    obj.vertices,
    obj.indices,
    program
) {

    private val mTexture = Texture(
        texturePath,
        program
    )

    override fun draw() {
        super.draw()

        mTexture.draw()

    }

}