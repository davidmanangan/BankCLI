package iam.davidmanangan.bankcli.prompt.settings;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class BankCLIPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Bank CLI > ", 
            AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
        );
    }
}