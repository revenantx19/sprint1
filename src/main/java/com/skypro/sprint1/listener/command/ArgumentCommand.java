package com.skypro.sprint1.listener.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ArgumentCommand implements Command {

    private String[] args;
    public ArgumentCommand(String[] args) {
        this.args = args;
    }
}
