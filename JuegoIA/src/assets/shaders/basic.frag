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
	gl_FragColor = v_color * color;
}                                                                                                                                                                                                                                                                                                                                          