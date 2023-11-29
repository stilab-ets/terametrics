package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Comment;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.SyntaxTokenImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TokenVisitor {

  private TextEntropy textEntropy ;

  public TokenVisitor() {
    textEntropy = new TextEntropy();
  }

  public TextEntropy getTextEntropy() {
    return textEntropy;
  }

  public List<SyntaxTokenImpl> identifyTokens (Tree root) {
    List<SyntaxTokenImpl> elements = new ArrayList<>();
    if (root != null){
      if (root instanceof SyntaxTokenImpl) {
        elements.add((SyntaxTokenImpl) root);
      }
      for (Tree child: root.children()) {
        elements.addAll( identifyTokens(child) );
      }
    }
    return elements;
  }


  public List<String> tokenizer(Tree root) {
    List<SyntaxTokenImpl>  tokens  = this.identifyTokens(root);
    List<String> values = new ArrayList<>();
    for (SyntaxTokenImpl token: tokens) {
      if (!token.value().trim().isEmpty()) {
        values.add(token.value());
      }
    }
    return values;
  }

  public List<Character> textualize(Tree root) {
    List<SyntaxTokenImpl> tokens = this.identifyTokens(root);
    List<Character> values = new ArrayList<>();

    for (SyntaxTokenImpl token : tokens) {
      if (!Objects.equals(token.value(), "")) {
        char[] chars = token.value().toCharArray();
        for (char c : chars) {
          if ((!Character.isWhitespace(c)) && (c != ' ') ) {
              values.add(c);
          }
        }

        if (!token.comments().isEmpty()) {
          for (Comment comment : token.comments()) {
            char[] commentChars = comment.value().toCharArray();
            for (char c : commentChars) {
              if (!Character.isWhitespace(c)) {
                values.add(c);
              }
            }
          }
        }
      }
    }
    return values;
  }




}
