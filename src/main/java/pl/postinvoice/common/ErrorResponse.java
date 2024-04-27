package pl.postinvoice.common;

import java.util.Objects;

public record ErrorResponse(String msg) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ErrorResponse) obj;
        return Objects.equals(this.msg, that.msg);
    }

    @Override
    public String toString() {
        return "ErrorResponse[" +
                "msg=" + msg + ']';
    }


}
