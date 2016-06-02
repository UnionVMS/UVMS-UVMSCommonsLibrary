package eu.europa.ec.fisheries.uvms.domain;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BaseEntityTest extends BaseUnitilsTest {

    @Test
    public void test(){
        TestEntity testEntity = new TestEntity();
        List<Field> properties = testEntity.listMembers();

        assertEquals(2, properties.size());

        Field field = properties.get(0);
        assertEquals("property1", field.getName());
        assertEquals("String", field.getType().getSimpleName());

        field = properties.get(1);
        assertEquals("property2", field.getName());
        assertEquals("String", field.getType().getSimpleName());

    }

    private class TestEntity extends BaseEntity {

        public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
        private static final String TEST = "rfmoEntity.ByCoordinate";

        private String property1;
        private String property2;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }
    }
}
