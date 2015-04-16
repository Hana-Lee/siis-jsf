package kr.co.leehana.siis.type;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:37.
 *
 * @author Lee Hana
 */
public enum SearchType {
    NAME("1", "제목"), AUTHOR("4", "저자");

    private String code;
    private String label;

    SearchType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
