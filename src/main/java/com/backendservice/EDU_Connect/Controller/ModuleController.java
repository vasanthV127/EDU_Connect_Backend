
package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.Module;
import com.backendservice.EDU_Connect.payload.request.ModuleDTO;
import com.backendservice.EDU_Connect.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepo;

    // Retrieve all modules with their resources as DTOs
    @GetMapping
    public List<ModuleDTO> getAllModules() {
        return moduleRepo.findAll().stream()
                .map(module -> new ModuleDTO(
                        module.getId(),
                        module.getName(),
                        module.getSemester(),
                        module.getResources() // Include resources
                ))
                .collect(Collectors.toList());
    }

    // Retrieve a single module with its resources
    @GetMapping("/{id}")
    public ModuleDTO getModuleById(@PathVariable Long id) {
        return moduleRepo.findById(id)
                .map(module -> new ModuleDTO(
                        module.getId(),
                        module.getName(),
                        module.getSemester(),
                        module.getResources() // Include resources
                ))
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + id));
    }

    // Create a new module (resources are added separately via TeacherController)
    @PostMapping
    public String createModule(@RequestBody Module module) {
        moduleRepo.save(module);
        return "Module created successfully!";
    }

    // Delete a module
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        try {
            moduleRepo.deleteById(id);
            return ResponseEntity.ok("Module deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Module not found with ID: " + id);
        }
    }

    // Update a module (only name and semester, resources managed separately)
    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id, @RequestBody ModuleDTO moduleDTO) {
        return moduleRepo.findById(id)
                .map(module -> {
                    module.setName(moduleDTO.getName());
                    module.setSemester(moduleDTO.getSemester());
                    Module updatedModule = moduleRepo.save(module);
                    return ResponseEntity.ok(new ModuleDTO(
                            updatedModule.getId(),
                            updatedModule.getName(),
                            updatedModule.getSemester(),
                            updatedModule.getResources() // Return updated module with resources
                    ));
                })
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + id));
    }
}