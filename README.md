# Autor

   * [Fernando Barrera Barrera](https://github.com/fernando-b15) :guitar:
   
## Link Front-End
  * [Front-End](https://github.com/fernando-b15/LAB10-Front-IETI)
  
## Test

## Test Part1

Primero vamos a probar la implementacion del metodo  getFileByName con la imagen lion.jpg que insertamos en la base de datos al ejecutar el servidor

![img1](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test1.PNG)

Ahora vamos a probar la implementacion del metodo handleFileUpload cargando una imagen local a la base de datos desde consola

![img2](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test4.PNG)

Vemos que el resultado de la peticion fue 200 asi que la imagen con el nombre perro.png fue cargada exitosamente a la base de datos ahora procedemos a ver la imagen en el servidor con el nombre perro.png 

![img3](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test5.PNG)

## Test Part2

Una vez que  logramos conectar el front con el servidor del back vamos a realizar una prueba e intentar cargar una imagen desde la vista newTask y con el nuevo campo para seleccionar archivo cargamos  una imagen llamada odst.jpg

![img4](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test3.PNG)

Vemos que la peticion se completo exitosamente asi que vamos a comprobar si la imagen quedo en el servidro del back y vemos que se cargo exitosamente

![img5](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test2.PNG)

## Test Part3

Ahora ya que terminamos completamente la conexion del front con los controllers del back vamos a realizar una prueba completa ,primero entramos a la vista home del front y vemos que inicalmente no tenemos tareas

![img6](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test6.PNG)

Ahora vamos a proceder a realizar el registro de la primera tarea con una imagen

![img7](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test7.PNG)

Regresamos al panel inicial y vemos que efectivamente se creo la nueva tarea con su respectiva imagen

![img8](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test8.PNG)

Ahora vamos a registrar otra tarea con otra imagen diferente a la anterior

![img9](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test9.PNG)

Ahora volvemos al home y vemos que efectivamente se inserto la nueva tarea con su respectiva imagen


![img10](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test10.PNG)

Ahora vamos a insertar la ultima tarea pero en vez de cargarle una imagen vamos a cargarleun documento pdf

![img11](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test11.PNG)

Ahora vamos al home y vemos que se inserto la nuava tarea y que en vez de una imagen tiene un icono de documento pdf

![img12](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test12.PNG)

Ahora al darle click al icono del documente pdf este no redirije al servidor del back donde esta el documento pdf y nos muestra el documento pdf en el buscador

![img13](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test13.PNG)

Por ultimo verficamos en el controller del back y vemos que efectivamente se resgistraron la tres tareas que resgistramos anteriormente


![img14](https://github.com/fernando-b15/LAB10-IETI/blob/master/img/test14.PNG)


## Part 1: Implement a File uploader API

1. Clone this project and change the Database configuration with your own connection string in the following file:
        
    *application.yml*:
    ``` yaml
    spring:
      data:
        mongodb:
          uri: mongodb+srv://<username>:<password>@ieti-dzkk5.mongodb.net/test?retryWrites=true&w=majority 
    ```

*Note:* Don't forget to add your IP address in the IP Whitelist of your MongoDb Atlas cluster before attempting to run your application. 

2. Run the project.

3. Go to your database and verify that the file was created under a fs.files and fs.chunks documents. 

4. Go to the *RESTController* class and inject the *GridFsTemplate* bean by using the *@Autowired* annotation.

5. Implement the *getFileByName* method:

    * Find the file with the following code:
        ````Java
          GridFSFile file = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("filename").is(filename)));
        ````
    * If the file does not exist return a Not Found HTTP status response:
    
        ````Java
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ````
    * If the file exist return the resource:
        ````Java
                GridFsResource resource = gridFsTemplate.getResource(file.getFilename());
                return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(resource.getContentType()))
                    .body(new InputStreamResource(resource.getInputStream()));
        ````    
    
5. Verify that the *getFileByName* endpoint works and it does returns the *lion.jpeg* file created on step 2 when requested by name.


6. Implement the *handleFileUpload* method. Make sure you return the static url to access the uploaded file by using the *getFileByName* endpoint.

    ````Java
       //Stores the file into MongoDB
        gridFsTemplate.store(file.getInputStream(), fileName, file.getContentType());
    ````

## Part 2: Integrate file uploader with React JS project

1. Using the TODO List application implemented in previous labs, add the following input to the Todo form:

    ````Javascript
        <input type="file" id="file" onChange={this.handleInputChange}/>
    ````
    
2. Implement and bind the *handleInputChange* method:

    ````Javascript
         handleInputChange(e) {
                this.setState({
                    file: e.target.files[0]
                });                
            }
    ````

3. Change the *handleSubmit* method so it uploads the file first:

    ````Javascript
    
            let data = new FormData();
            data.append('file', this.state.file);
    
            this.axios.post('files', data)
                .then(function (response) {
                    console.log("file uploaded!", data);
            })
            .catch(function (error) {
                console.log("failed file upload", error);
            });
    
    ```` 
4. Run your React project and verify that the file uploader works.


## Part 3: Upgrade your Todo to accept files

1. Inject the *TodoRepository* into the *RESTController* using *@Autowired* annotation.

2. Implement the *createTodo* and *getTodoList* methods of the *RESTController*.

3. Modify the *handleSubmit* method so it does call the API to create the *Todo* entry on the server and database:

    * Make asynchronous calls to upload file to the server
    * Once the file upload promise is fulfilled, then save the Todo entry using the *POST* method of the API.
    * Remember to save the *this* context into a variable to use it into the nested scopes!
   
4. Modify the *Todo* component on your React project so that it displays the Todo image:

    ````Javascript
          <td>{this.props.fileUrl ? <img src={this.props.fileUrl} /> : <div/>}</td>
    ```` 

5. If you have not done it yet, implement and call the method to load the Todo list from the server:

    ```javascript
         loadDataFromServer() {
        
                let that = this;
        
                this.axios.get("todo").then(function (response) {
                    console.log("This is my todolist:  ", response.data);
                    that.setState({items: response.data})
                })
                    .catch(function (error) {
                        console.log(error);
                    });
            }

    ```    
    6. Implement the logic for the following scenario: if the file is not an image but a pdf file, then a file icon appears on the Todo component. When the file icon is clicked then the user gets redirected to the download page.
    
7. Verify that the project works as expected.
