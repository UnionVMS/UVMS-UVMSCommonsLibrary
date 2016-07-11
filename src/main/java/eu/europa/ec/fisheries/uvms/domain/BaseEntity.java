/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.domain;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@MappedSuperclass
@ToString
@EqualsAndHashCode
public class BaseEntity implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    protected BaseEntity(){
        this.id = null;
    }

    public Long getId() {
        return id;
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