#ifdef GL_ES
precision mediump float; 
#endif 
varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture; 
void main() 
{
	vec4 color = texture2D(u_texture, v_texCoords);
	vec4 trans = vec4(1,0,1,color.a);
	if(length(trans-color) < 0.1)
	{
		color = color * vec4(0,0,0,0);
	}
	color = v_color * color;
	float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
	gl_FragColor = vec4(gray, gray, gray, color.a);
}                                                                                                                                                                                                                                                                                                                                          