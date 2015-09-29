package eu.europa.ec.fisheries.uvms.rest.mapper;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.exception.ModelMarshallException;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSONMarshallerTest {

    @Test
    public void testMarshallJsonStringToObject() throws IOException, ModelMarshallException {

        JSONMarshaller jsonMarshaller = new JSONMarshaller();
        URL url = Resources.getResource("testMarshallJsonStringToObject.json");
        String json = Resources.toString(url, Charsets.UTF_8);
        User user = jsonMarshaller.marshallJsonStringToObject(json, User.class);
        assertEquals("greg", user.getName());
        assertEquals(29, user.getAge());
        assertEquals(3, user.getMessages().size());

    }

    private static class User {

        private int age = 29;
        private String name = "greg";
        private List<String> messages = new ArrayList<String>() {
            {
                add("msg 1");
                add("msg 2");
                add("msg 3");
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }
    }
}
