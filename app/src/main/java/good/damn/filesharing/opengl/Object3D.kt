package good.damn.filesharing.opengl

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer
import java.util.Vector

class Object3D(
    val vertices: FloatBuffer,
    val indices: ShortBuffer
) {
    companion object {
        fun createFromResources(
            resourceId: Int,
            context: Context
        ): Object3D {
            val vertices: Vector<Float> = Vector()
            val normals: Vector<Float> = Vector()
            val textures: Vector<Float> = Vector()
            val faces: Vector<String> = Vector()

            var reader: BufferedReader? = null

            try {
                val inStream = InputStreamReader(
                    context.resources.openRawResource(
                        resourceId
                    )
                )
                reader = BufferedReader(inStream)

                // read file until EOF
                var line = reader.readLine()
                while (line != null) {
                    val parts = line.split("\\s+".toRegex())
                    when (parts[0]) {
                        "v" -> {
                            // vertices
                            vertices.add(parts[1].toFloat())
                            vertices.add(parts[2].toFloat())
                            vertices.add(parts[3].toFloat())
                        }
                        "vt" -> {
                            // textures
                            textures.add(parts[1].toFloat())
                            textures.add(parts[2].toFloat())
                        }
                        "vn" -> {
                            // normals
                            normals.add(parts[1].toFloat())
                            normals.add(parts[2].toFloat())
                            normals.add(parts[3].toFloat())
                        }
                        "f" -> {
                            // faces: vertex/texture/normal
                            faces.add(parts[1])
                            faces.add(parts[2])
                            faces.add(parts[3])
                        }
                    }
                    line = reader.readLine()
                }
            } catch (e: Exception) {
                // cannot load or read file
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: Exception) {
                        //log the exception
                    }
                }
            }

            val mNormals = FloatArray(faces.size * 3)
            val mTexCoords = FloatArray(faces.size * 2)
            val mVertices = FloatArray(faces.size * 3)
            val mIndices = ShortArray(faces.size)

            var texIndex = 0
            var posIndex = 0
            var normIndex = 0

            for ((index, face) in faces.withIndex()) {
                val parts = face.split("/")
                val vertexIndex = (parts[0].toShort() - 1).toShort()
                mIndices[index] = index.toShort()

                var i = 3 * vertexIndex
                mVertices[posIndex++] = vertices[i++]
                mVertices[posIndex++] = vertices[i++]
                mVertices[posIndex++] = vertices[i]

                i = 2 * (parts[1].toInt() - 1)
                mTexCoords[texIndex++] = textures[i++]
                mTexCoords[texIndex++] = 1 - textures[i]

                i = 3 * (parts[2].toInt() - 1)
                mNormals[normIndex++] = normals[i++]
                mNormals[normIndex++] = normals[i++]
                mNormals[normIndex++] = normals[i]
            }

            val byteBuffer =
                ByteBuffer.allocateDirect(mVertices.size * 4)
            byteBuffer.order(ByteOrder.nativeOrder())
            val mVertexBuffer = byteBuffer.asFloatBuffer()
            mVertexBuffer.put(mVertices)
            mVertexBuffer.position(0)

            val drawByteBuffer: ByteBuffer =
                ByteBuffer.allocateDirect(mIndices.size * 2)
            drawByteBuffer.order(ByteOrder.nativeOrder())
            val mIndicesBuffer = drawByteBuffer.asShortBuffer()
            mIndicesBuffer.put(mIndices)
            mIndicesBuffer.position(0)

            return Object3D(
                mVertexBuffer,
                mIndicesBuffer
            )
        }

    }
}