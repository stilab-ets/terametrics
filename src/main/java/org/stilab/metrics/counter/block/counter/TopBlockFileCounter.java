package org.stilab.metrics.counter.block.counter;

import org.stilab.interfaces.BlockTypeCounter;
import org.sonar.iac.common.api.tree.Tree;

public class TopBlockFileCounter implements BlockTypeCounter {
  @Override
  public int blockTypeCounter(Tree tree) {

    DataCounter data = new DataCounter();
    LocalsCounter localsCounter = new LocalsCounter();
    ModuleCounter moduleCounter = new ModuleCounter();
    OutputCounter outputCounter = new OutputCounter();
    ProviderCounter providerCounter = new ProviderCounter();
    ResourceCounter resourceCounter = new ResourceCounter();

    return data.blockTypeCounter(tree) + localsCounter.blockTypeCounter(tree) +
    moduleCounter.blockTypeCounter(tree) + outputCounter.blockTypeCounter(tree) +
    providerCounter.blockTypeCounter(tree) + resourceCounter.blockTypeCounter(tree);
  }

}
