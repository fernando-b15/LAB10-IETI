package eci.ieti.controller;


import eci.ieti.data.TodoRepository;
import eci.ieti.data.model.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

@RequestMapping("api")
@RestController
public class RESTController {


   //TODO inject components (TodoRepository and GridFsTemplate)
	@Autowired
	TodoRepository todoRepository;
	@Autowired
	GridFsTemplate gridFsTemplate;
	
    @RequestMapping("/files/{filename}")
    public ResponseEntity<InputStreamResource> getFileByName(@PathVariable String filename) throws IOException {
    	try {
    		GridFSFile file = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("filename").is(filename)));
    		GridFsResource resource = gridFsTemplate.getResource(file.getFilename());
            return ResponseEntity.ok()
                .contentType(MediaType.valueOf(resource.getContentType()))
                .body(new InputStreamResource(resource.getInputStream()));
    	}
    	catch(Exception e) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
       

    }

    @CrossOrigin("*")
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        redirectAttributes.addFlashAttribute("message","File "+ file.getOriginalFilename() + "successfully uploaded");
        return "http://localhost:8080/api/files/"+file.getOriginalFilename();
    }

    @CrossOrigin("*")
    @PostMapping("/todo")
    public ResponseEntity<?> createTodo(@RequestBody Todo todo) throws ServletException {
    	try {
    		todoRepository.save(todo);
    		return new ResponseEntity<>(HttpStatus.CREATED);
    	}catch(Exception e) {
    		throw new ServletException("Error al crear todo");
    	}
    }

    @CrossOrigin("*")
    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoRepository.findAll();
    }

}
