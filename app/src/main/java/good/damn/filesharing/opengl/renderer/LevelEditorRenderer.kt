package good.damn.filesharing.opengl.renderer

import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.camera.BaseCamera
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import good.damn.filesharing.opengl.EditorMesh
import good.damn.filesharing.opengl.Mesh
import good.damn.filesharing.opengl.Object3D
import good.damn.filesharing.opengl.light.DirectionalLight
import good.damn.filesharing.utils.AssetUtils
import good.damn.filesharing.utils.ShaderUtils
import java.util.LinkedList

class LevelEditorRenderer
: GLSurfaceView.Renderer {

    private val mCamera = BaseCamera()

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private val meshesEditor = LinkedList<EditorMesh>()
    private val meshes = LinkedList<Mesh>()

    private lateinit var mDirectionalLight: DirectionalLight
    private lateinit var meshToDraw: Mesh

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        mProgram = glCreateProgram()

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_VERTEX_SHADER,
                AssetUtils.loadString(
                    "shaders/vert.glsl"
                )
            )
        )

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_FRAGMENT_SHADER,
                AssetUtils.loadString(
                    "shaders/frag.glsl"
                )
            )
        )

        glLinkProgram(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        meshToDraw = Mesh(
            Object3D.createFromAssets(
                "objs/plane.obj"
            ),
            "textures/grass.jpg",
            mProgram,
            mCamera
        )

        mDirectionalLight = DirectionalLight(
            mProgram
        )

        glEnable(
            GL_DEPTH_TEST
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height

        mCamera.setPerspective(
            width,
            height
        )

        mCamera.setPosition(
            0f,
            0f,
            -0.5f
        )

    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        glClear(GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0.0f,
                0.0f,
            0.0f,
            1.0f
        )

        mDirectionalLight.draw()
        meshToDraw.draw()
    }

    fun onTouchDown(
        x: Float,
        y: Float
    ) {

    }

    fun onTouchMove(
        x: Float,
        y: Float
    ) {
        mCamera.setPosition(
            x / mWidth,
            y / mHeight,
            0f
        )
    }

    fun onTouchUp(
        x: Float,
        y: Float
    ) {

    }

}