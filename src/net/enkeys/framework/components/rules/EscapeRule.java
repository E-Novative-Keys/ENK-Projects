package net.enkeys.framework.components.rules;

public class EscapeRule extends Rule
{
    public EscapeRule(String message)
    {
        super(message);
    }

    @Override
    public boolean validate(Object object)
    {
        return (object instanceof String && getMessage().contains((String)object));
    }
}
