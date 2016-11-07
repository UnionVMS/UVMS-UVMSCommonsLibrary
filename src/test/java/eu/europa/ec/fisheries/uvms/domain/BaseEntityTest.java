/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.domain;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

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
