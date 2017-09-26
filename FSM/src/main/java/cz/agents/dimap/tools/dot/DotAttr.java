package cz.agents.dimap.tools.dot;

public class DotAttr {

	public final String keyword;
	public final String value;
	
	public DotAttr(String keyword, String value) {
		this.keyword = keyword;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return keyword+" = \""+value+"\"";
	}

	public static DotAttr attr(String key, String val) {
		return new DotAttr(key, val);		
	}
	
	public static DotAttr color(String val) {
		return new DotAttr("color", val);		
	}

	public static DotAttr fillcolor(String val) {
		return new DotAttr("fillcolor", val);		
	}
	
	public static DotAttr shape(String val) {
		return new DotAttr("shape", val);		
	}

	public static DotAttr style(String val) {
		return new DotAttr("style", val);		
	}

	public static DotAttr label(String val) {
		return new DotAttr("label", val);		
	}

	public static DotAttr fontcolor(String val) {
		return new DotAttr("fontcolor", val);		
	}

    public static DotAttr dir(String val) {
        return new DotAttr("dir", val);       
    }

}
