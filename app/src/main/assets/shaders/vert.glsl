attribute vec4 position;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 camera;

varying lowp vec3 posOut;
varying lowp vec2 texCoordOut;

void main() {
    vec4 coord = camera * model * position;
    gl_Position = projection * coord;
    posOut = coord.xyz;
    texCoordOut = texCoord;
}