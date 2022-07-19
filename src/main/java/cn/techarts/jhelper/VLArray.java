package cn.techarts.jhelper;

/**
 * The Variable Length Array implementation that just supports INT elements.
 */
public final class VLArray {
	private int[] data;
	public int length;
	private int maxIndex;
	
	public static VLArray init(int len) {
		return new VLArray(len);
	}
	
	private VLArray(int len) {
		data = new int[len <= 0 ? 32 : len];
		this.length = data.length;
	}
	
	public int get(int index) {
		return this.data[index];
	}
	
	public void put(int index, int item) {
		if(index >= this.length) {
			extendsCapacity(index);
		}
		this.data[index] = item;
		
		if(index > maxIndex) {
			maxIndex = index;
		}
	}
	
	/**
	 * @return Returns the original INT array with actual length.
	 * */
	public int[] get() {
		var len = maxIndex + 1;
		var result = new int[len];
		System.arraycopy(data, 0, result, 0, len);
		return result;
	}
	
	/**
	 * Slice the array from the specific indexes start(inclusive) to end(inclusive).
	 * @see Slicer.slice
	 * */
	public int[] get(int bgn, int end) {
		return Slicer.slice(this.data, bgn, end);
	}
	
	private void extendsCapacity(int min) {
		var tmp = this.data;
		int ext = min + (min <= 512 ? (min >> 1) : (min >> 2));
		length += length <= 512 ? (length >> 1) : (length >> 2);
		
		if(length <= ext) length = ext;
		
		this.data = new int[this.length]; //Resize
		System.arraycopy(tmp, 0, data, 0, tmp.length);
	}
}