#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 outTextCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
	vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * mvPos;
	outTextCoord = texCoord;
	mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
	mvVertexPos = mvPos.xyz;
}