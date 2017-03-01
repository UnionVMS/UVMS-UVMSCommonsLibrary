/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@ToString
@EqualsAndHashCode
public class BaseEntity implements Serializable {

    protected BaseEntity(){
    }

    public List<Field> listMembers(){
        List<Field> fields = new ArrayList<>();
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if(!field.getName().contains("this") &&
                        field.getModifiers() != Modifier.STATIC + Modifier.PUBLIC + Modifier.FINAL
                        && field.getModifiers() != Modifier.STATIC + Modifier.PRIVATE + Modifier.FINAL) {
                    fields.add(field);
                }
            }
        } catch (Exception e){
            //Handle your exception here.
        }
        return fields;
    }

}
