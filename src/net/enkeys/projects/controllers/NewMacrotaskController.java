/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.NewMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 *
 * @author Worker
 */
public class NewMacrotaskController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final NewMacrotaskView view = (NewMacrotaskView)super.view;
    private final HashMap<String, String> project;

    public NewMacrotaskController(EApplication app, EView view, HashMap<String, String> project) 
    {
        super(app, view);
        
        this.project = project;
        
        this.view.getBack().addActionListener(backButtonListener());
    }

    private ActionListener backButtonListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }   
}
