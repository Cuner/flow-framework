package org.cuner.flowframework.support;

/**
 * Created by houan on 18/7/23.
 */
public class Comparator {

    public static boolean validate(Object left, String right, CompareType compareType) {
        if (null == left || null == right) {
            return false;
        }
        switch (compareType) {
            case EQ:
                return left.toString().equals(right);
            case NE:
                return !left.toString().equals(right);
            case GT:
                return (Integer) left - Integer.parseInt(right) > 0;
            case GE:
                return (Integer) left - Integer.parseInt(right) >= 0;
            case LT:
                return (Integer) left - Integer.parseInt(right) < 0;
            case LE:
                return (Integer) left - Integer.parseInt(right) <= 0;
            case IN:
                //全部转成字符串比较
                String[] values = right.split(",");
                if (values.length == 0){
                    return false;
                }

                for (String value : values){
                    if (left.toString().trim().equals(value.trim())){
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }


    public enum CompareType {
        EQ("等于"),
        NE("不等于"),
        LT("小于"),
        LE("小于或者等于"),
        GT("大于"),
        GE("大于或者等于"),
        IN("包含于");

        private String desc;

        CompareType(String desc) {
            this.desc = desc;
        }

        public static CompareType getCompareType(String name) {
            for (CompareType compareType : CompareType.values()) {
                if (compareType.name().equalsIgnoreCase(name)) {
                    return compareType;
                }
            }
            return null;
        }

    }
}
