package artxew.project.layers.example.dto.req;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BadRequestReqDto {

    @NotNull
    private String param;

    @NotEmpty
    private String[] args;

    @Data
    public static class A {

        @NotEmpty
        private String aa;

        @NotNull
        private String ab;

        @NotEmpty
        private String ac;
    }
    @NotNull
    @Valid
    private A a;

    @Data
    public static class B {

        @NotNull
        private String ba;

        @NotEmpty
        private String bb;

        @NotNull
        private String bc;
    }
    @NotNull
    @Valid
    private B b = new B();

    @Data
    public static class C {

        @NotNull
        private String ca;

        @NotEmpty
        private String cb;

        @NotNull
        private String cc;

        @Data
        public static class D {

            @NotNull
            private String cda;

            @NotEmpty
            private String cdb;

            @NotNull
            private String cdc;
        }
        @NotNull
        @Valid
        private D[] cd = new D[4];
    }
    @NotNull
    @Valid
    private C c = new C();
}