package pl.postinvoice.Util;

import org.springframework.data.domain.Page;

import java.util.function.Supplier;

public class LogUtil {
    public static void logPage(Supplier<Page<?>> pageSupplier, String methodName) {
        System.out.println("*****************************" + methodName + "******************************");

        Page<?> postPage = pageSupplier.get();


        System.out.println("getContent: " + postPage.getContent());
        System.out.println("getTotalPages: " + postPage.getTotalPages());
        System.out.println("getTotalElements: " + postPage.getTotalElements());
        System.out.println("getTotalElements: " + postPage.getNumber());
        System.out.println("getNumberOfElements: " + postPage.getNumberOfElements());
        System.out.println("getSize: " + postPage.getSize());
    }
}
