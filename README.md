# Security
SpringBoot中配置SpringSecurity，提供安全的RestfulAPI

.Restful一种编码规范    
.url锁定资源   
.http的method指定如何操作资源，并以json的方式返回结果    

# 一、使用SpringMVC开发RestfulAPI
 
## 1.简单RestfulAPI编写
### (1)编写针对RestfulAPI的测试用例
RestfulAPI
```java
@RestController
public class UserController {
    @GetMapping("/user")
    public List<User> getUser(@RequestParam String name){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(name, 12));
        list.add(new User("B", 13));
        list.add(new User("C", 14));

        return list;
    }
}
```
针对性的测试用例
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    WebApplicationContext application;

    MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(application).build();
    }
    
    @Test
        public void whenQueryUserSuccess() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/user")
                    .param("name","A") 
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
        }
}

```
### (2)@JsonView注解
不同请求返回同一个json类型的JavaBean(单一对象或集合)可见的属性不同    
.使用接口来声明多个视图    
.在对象的get方法上指定视图,使用lombok就标注在属性上    
.在Controller的方法上指定视图      

在JavaBean中声明不同的视图，并标注哪些属性属于哪种视图
```java
@Getter
@Setter
public class User {

    public interface simpleView{}

    public interface detailView  extends simpleView{}


    @JsonView(simpleView.class)
    private String name;

    @JsonView(simpleView.class)
    private Integer age;

    @JsonView(detailView.class)
    private String cardID;

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }


}
```
Controller中标注不同的请求返回不同的JavaBean视图
```java
@RestController
public class UserController {
    @GetMapping("/user")
    @JsonView(User.simpleView.class)
    public List<User> getUsers(@RequestParam String name){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(name, 12));
        list.add(new User("B", 13));
        list.add(new User("C", 14));
        return list;
    }


    @GetMapping("/user/{id}")
    @JsonView(User.detailView.class)
    public User getUserInfo(@PathVariable("id") Integer id){
        return new User("tom", id);
    }
}

```  

