package com.takeaway.authorization.runtime.businessservice.control;

import com.takeaway.authorization.UnitTestSuite;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko Date: 12.11.2019 Time: 14:18
 *
 * <p>
 */
class BeanToolTest extends UnitTestSuite
{
    @Getter
    @Setter
    private static class A
    {
        private String one;

        private Integer two;

        private Integer three;

        private Integer four;
    }

    @Getter
    @Setter
    private static class B
    {
        private String one;

        private Integer two;

        private String three;

        private Integer four;

        private Integer five;
    }

    @Test
    void whenCopyNonNullProperties_thenPass()
    {
        // Arrange
        BeanToolTest.A a = new BeanToolTest.A();
        a.one = "A.one";
        a.two = 2;
        a.four = 4;

        BeanToolTest.B b = new BeanToolTest.B();
        b.one = "B.one";
        b.two = 22;
        b.three = "B.three";
        b.four = 44;

        // Act
        BeanTool.copyNonNullProperties(a, b, "four");

        // Assert
        assertThat(b.getOne()).isEqualTo(a.getOne());
        assertThat(b.getTwo()).isEqualTo(a.getTwo());
        assertThat(b.getThree()).isEqualTo(b.getThree());
        assertThat(b.getFour()).isEqualTo(b.getFour());
    }
}
