package com.example.backend_pfm.controller;

import com.example.backend_pfm.beans.Action;
import com.example.backend_pfm.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @GetMapping
    public List<Action> getAllActions() {
        return actionService.getAllActions();
    }

    @GetMapping("/{id}")
    public Action getActionById(@PathVariable int id) {
        return actionService.getActionById(id);
    }

    @GetMapping("/receiver/{receiverId}")
    public List<Action> getActionsByReceiverId(@PathVariable int receiverId) {
        return actionService.getActionsByReceiverId(receiverId);
    }

    @GetMapping("/donator/{donatorId}")
    public List<Action> getActionsByDonatorId(@PathVariable int donatorId) {
        return actionService.getActionsByDonatorId(donatorId);
    }

    @PostMapping
    public Action createAction(@RequestBody Action action) {
        return actionService.saveAction(action);
    }

    @PutMapping("/{id}")
    public Action updateAction(@PathVariable int id, @RequestBody Action actionDetails) {
        Action existingAction = actionService.getActionById(id);
        if (existingAction != null) {
            existingAction.setDonator(actionDetails.getDonator());
            existingAction.setReceiver(actionDetails.getReceiver());
            existingAction.setAltitude(actionDetails.getAltitude());
            existingAction.setLongitude(actionDetails.getLongitude());
            existingAction.setValid(actionDetails.isValid());
            existingAction.setDescription(actionDetails.getDescription());
            return actionService.saveAction(existingAction);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteAction(@PathVariable int id) {
        actionService.deleteAction(id);
    }
}
