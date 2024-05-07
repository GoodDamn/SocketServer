precision mediump float;

struct Light {
    lowp vec3 color;
    lowp float intensity;
};

uniform sampler2D texture;
uniform Light light;

varying lowp vec3 posOut;
varying lowp vec2 texCoordOut;

void main() {

    lowp vec4 ambColor = vec4(light.color, 1.0) * light.intensity;

    gl_FragColor = texture2D(
        texture,
        texCoordOut
    ) * ambColor;

}