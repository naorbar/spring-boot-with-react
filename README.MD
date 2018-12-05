# This is a guide on how to integrate a spring-boot server-side application with React client-side

It uses: 

- spring-boot 2
- React
- WebMVC and JPA

## To create the server-side:

- Use spring initializer to create a spring-boot application with JPA, H2, WEB and DEVTOOLS
- Import the spring-boot application to the IDE as an existing Maven project
- Create a simple controller and a manager with CRUD Repository

e.g.
```
@RestController
public class HrController {

	@Autowired
	private HrManager hrManager;
	
	@GetMapping(path = "/test")
	public String test() {
		return "HELLO, The server time is " + new Date();
	}
	
	@GetMapping(path = "/users_static")
	public Iterable<User> getUsersStaticList() {
		return hrManager.getAllUsersStatic();
	}
	
	@GetMapping(path = "/users")
	public Iterable<User> getUsers() {
		return hrManager.getAllUsers();
	}
	
	@GetMapping(path = "/users/{id}")
	public Optional<User> getUserById(@PathVariable String id) {
		return hrManager.getUserById(id);
	}
}
```

- Start the server and verify that the controller responds at http://localhot:8080/test, http://localhot:8080/users and http://localhot:8080/users/{id} 
- Check that the H2 console is available at http://localhost:8080/h2-console

Additional Settings in application.properties:
```
# H2
spring.h2.console.enabled=true

# Additional Web debug prints:
logging.level.web=DEBUG
```

## To create the client-side:

- `cd` to the root folder of the project, e.g. `C:\temp\spring-boot-with-react`
- Use `npx create-react-app frontend` to create a folder called `frontend` inside the project
- Use `cd frontend` and `npm start` to start the client at http://localhot:3000

Now we have both server running at http://localhost:8080 and client running at http://localhot:3000, but when the client tries to connect to the server on the same machine, you may get a CORS exception, i.e. 
```
Access to fetch at 'http://localhost:8080/test' from origin 'http://localhost:3000' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource...
```
Note: on production it won't happen since I use the same server (spring-boot's tomcat) to server both UI and Server. 

### To avoid this on development you can enable CORS on the browser or the server or or set up a proxy in the client

#### Setting the browser for development purposes:
Launch the browser with `--disable-web-security` flag
E.g. `chrome.exe --disable-web-security`
or add this extension:
https://chrome.google.com/webstore/detail/allow-control-allow-origi/nlfbmbojpeacfghkpbjhddihlkkiljbi/related?hl=en-US

#### Setting the proxy for development purposes:
Note: the client-side development will be done on http://localhot:3000 (as usual), so to enable calling the rest APIs from the client to the server, we add the following proxy settings:

- `cd frontend` and `npm install http-proxy-middleware`
- Create a new file in `frontend\src` folder: setupProxy.js

```
const proxy = require('http-proxy-middleware');

module.exports = function(app) {
	
	app.use('/api', proxy({
	    target: 'http://localhost:8080', 
	    logLevel : 'debug',
	    changeOrigin: true,
	    pathRewrite: {
	        '^/api' : '/'
	    }
	}));
};
```
- Start the frontend app with `npm start`
- Browse to http://localhot:3000/api/test to verify that the proxy is working as expected

## Setting the pom.xml for production purposes:
Since spring-boot application serves static content if you put it into the classes/public of the .jar file, we use 2 maven plugins which:
1. Create a production build of the frontend with `npm run build` command (frontend-maven-plugin) >>> the artifacts will be located in the `frontend/build` folder
2. Copy the production artifacts from `frontend/build` to `target/classes/public` before packaging to jar (maven-antrun-plugin) >>> the artifacts will be located in the jar file under: `BOOT-INF\classes\public`

- Add the following plugins to the pom.xml
```
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.6</version>
    <configuration>
        <workingDirectory>frontend</workingDirectory>
        <installDirectory>target</installDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <configuration>
                <nodeVersion>v8.11.3</nodeVersion>
                <npmVersion>5.6.0</npmVersion>
            </configuration>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm run build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <executions>
        <execution>
            <phase>generate-resources</phase>
            <configuration>
                <target>
                    <copy todir="${project.build.directory}/classes/public">
                        <fileset dir="${project.basedir}/frontend/build"/>
                    </copy>
                </target>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
- Run `java -jar <spring_boot_jar>` to start the server, e.g. java -jar spring-boot-with-react-0.0.1-SNAPSHOT.jar
- Browse to localhost:8080 and verify the frontend is loaded as expected