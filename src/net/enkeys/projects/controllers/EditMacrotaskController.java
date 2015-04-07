/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.controllers;

import java.util.HashMap;
import net.enkeys.framework.components.EController;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.NewMacrotaskView;

class EditMacrotaskController extends EController 
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final NewMacrotaskView view = (NewMacrotaskView) super.view;
    private final HashMap<String, String> project;
    private final HashMap<String, String> data;

    public EditMacrotaskController(ENKProjects app, 
                                   NewMacrotaskView view, 
                                   HashMap<String, String> project, 
                                   HashMap<String, String> data) 
    {
        super(app, view);
        
        this.project = project;
        this.data = data;
    }  
}
