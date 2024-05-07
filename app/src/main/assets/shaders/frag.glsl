precision mediump float;

struct Light {
    lowp vec3 color;
    lowp float ambient;
    lowp float intensity;
    lowp vec3 direction;
};

uniform sampler2D texture;
uniform Light light;

varying lowp vec3 posOut;
varying lowp vec3 normalOut;
varying lowp vec2 texCoordOut;

void main() {

    lowp vec3 ambColor = light.color * light.ambient;

    lowp vec3 normal = normalize(normalOut);

    lowp float diffFactor = max(-dot(normal, light.direction), 0.0);
    lowp vec3 diffColor = light.color * light.intensity * diffFactor;

    gl_FragColor = texture2D(
        texture,
        texCoordOut
    ) * vec4(ambColor + diffColor, 1.0);

}