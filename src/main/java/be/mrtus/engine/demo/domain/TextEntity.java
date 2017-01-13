package be.mrtus.engine.demo.domain;

import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.Texture;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TextEntity {

	private final int VERTICES_PER_QUAD = 4;
	private final float ZPOS = 0.0f;
	private final int col;
	private Model model;
	private final int row;
	private String text;
	private final Texture texture;
	private final Transform transform;

	public TextEntity(String text, String fontFile, int col, int row) {
		this.text = text;
		this.texture = new Texture(fontFile);
		this.col = col;
		this.row = row;
		this.model = this.buildModel(this.texture, this.col, this.row);
		this.transform = new Transform.Builder<>().build();
	}

	public void setPosition(float x, float y) {
		this.transform.setPosition(x, y, 0.0f);
	}

	public Model getModel() {
		return this.model;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		if(this.model != null){
			this.model.destroy();
		}
		this.model = this.buildModel(this.texture, this.col, this.row);
	}

	public Transform getTransform() {
		return this.transform;
	}

	private Model buildModel(Texture texture, int numCols, int numRows) {
		byte[] chars = this.text.getBytes(Charset.forName("ISO-8859-1"));
		int numChars = chars.length;
		List<Float> positions = new ArrayList();
		List<Float> textCoords = new ArrayList();
		List<Integer> indices = new ArrayList();
		float tileWidth = texture.getWidth() / numCols;
		float tileHeight = texture.getHeight() / numRows;
		for (int i = 0; i < numChars; i++) {
			byte currChar = chars[i];
			int col = currChar % numCols;
			int row = currChar / numCols;
// Build a character tile composed by two triangles
// Left Top vertex
			positions.add((float)i * tileWidth); // x
			positions.add(0.0f); //y
			positions.add(ZPOS); //z
			textCoords.add((float)col / (float)numCols);
			textCoords.add((float)row / (float)numRows);
			indices.add(i * VERTICES_PER_QUAD);
// Left Bottom vertex
			positions.add(i * tileWidth); // x
			positions.add(tileHeight); //y
			positions.add(ZPOS); //z
			textCoords.add(col / (float)numCols);
			textCoords.add((row + 1) / (float)numRows);
			indices.add(i * VERTICES_PER_QUAD + 1);
// Right Bottom vertex
			positions.add(i * tileWidth + tileWidth); // x
			positions.add(tileHeight); //y
			positions.add(ZPOS); //z
			textCoords.add((float)(col + 1) / (float)numCols);
			textCoords.add((float)(row + 1) / (float)numRows);
			indices.add(i * VERTICES_PER_QUAD + 2);
// Right Top vertex
			positions.add((float)i * tileWidth + tileWidth); // x
			positions.add(0.0f); //y
			positions.add(ZPOS); //z
			textCoords.add((float)(col + 1) / (float)numCols);
			textCoords.add((float)row / (float)numRows);
			indices.add(i * VERTICES_PER_QUAD + 3);
// Add indices por left top and bottom right vertices
			indices.add(i * VERTICES_PER_QUAD);
			indices.add(i * VERTICES_PER_QUAD + 2);
		}
		float[] floatArr = new float[positions.size()];
		for (int i = 0; i < positions.size(); i++) {
			floatArr[i] = positions.get(i);
		}
		int[] intArr = indices.stream().mapToInt(i -> i).toArray();
		textCoords.stream().map(t -> t).toArray();
		float[] floatArrText = new float[textCoords.size()];
		for (int i = 0; i < textCoords.size(); i++) {
			floatArrText[i] = textCoords.get(i);
		}
		return new Model.ModelBuilder().setPositions(floatArr, intArr).setTexture(texture, floatArrText).build();
	}
}
