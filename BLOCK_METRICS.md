| Metric Type | Metric Name                     | Description                                                  | Inspired From |
| ----------- | ------------------------------- | ------------------------------------------------------------ | ------------- |
| Meta        | "impacted_block_type"           | the block type + block name                                  |               |
| Meta        | "block"                         | the type of the block                                        |               |
| Meta        | "start_block"                   | the index start of block in the file                         |               |
| Meta        | "end_block"                     | the index end of block in the file                           |               |
| Meta        | "block_identifiers"             | the block identifier concats the block type, block name and its labels |               |
| Code        | "numComparisonOperators"        | total number of comparison operators in the block            |               |
| Code        | "avgComparisonOperators"        | average number of comparison operators in the block (can be considered as percentage in the case when we divise by the total number of attributes) |               |
| Code        | "maxComparisonOperators"        | max number of comparison operators that can be contained in an attribute belonging to the block |               |
| Code        | "numConditions"                 | total number of conditional expressions in the block         |               |
| Code        | "numAttrs"                      | total number of attributes in the block                      |               |
| Code        | "avgConditions"                 | average number of conditional expressions per attribute in the block |               |
| Code        | "maxConditions"                 | max number of conditional expressions per attribute in the block |               |
| Code        | "numLogiOpers"                  | total number of logical operators  in the block [**&&, \|\|, !**] |               |
| Code        | "avgLogiOpers"                  | average number of logical operators per attribute in the block |               |
| Code        | "maxLogiOpers"                  | max number of logical operators per attribute in the block   |               |
| Code        | "numDynamicBlocks"              | total number of dynamic blocks in the block                  |               |
| Code        | "numNestedBlocks"               | total number of nested blocks in the block                   |               |
| Code        | "numFunctionCall"                | total number of function invocations in the block            |               |
| Code        | "avgFunctionCall"               | average number of function invocations in the block per attribute |               |
| Code        | "maxFunctionCall"               | max number of function invocations in the block per attribute |               |
| Code        | "numParams"                     | total number of parameters in the block contained in the function invocation |               |
| Code        | "avgParams"                     | average number of parameters per function invocation         |               |
| Code        | "maxParams"                     | max number of parameters per function invocation             |               |
| Code        | "numHereDocs"                   | total number of [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) in the block [**<<EOF...EOF**] |               |
| Code        | "avgHereDocs"                   | average number of [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) in the block per attribute |               |
| Code        | "numLinesHereDocs"              | total number of lines contained in the [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) |               |
| Code        | "avgLinesHereDocs"              | average number of lines contained in the here-docs expressions |               |
| Code        | "maxLinesHereDocs"              | max number of lines contained in the here-docs expressions   |               |
| Code        | "numIndexAccess"                | number of Index Access expressions in the block              |               |
| Code        | "avgIndexAccess"                | average number of Index Access expressions per attribute in the block |               |
| Code        | "maxIndexAccess"                | max number of Index Access expressions per attribute in the block |               |
| Code        | "numLiteralExpression"          | total number of literal expressions in the block             |               |
| Code        | "numStringValues"               | total number of string values in the block *Hard-Coded*      |               |
| Code        | "numLoops"                      | total number of loops in the block                           |               |
| Code        | "avgLoops"                      | average number of loops per attribute (in the block)         |               |
| Code        | "maxLoops"                      | max number of loops per attribute (in the block)             |               |
| Code        | "numMathOperations"             | total number of math operations in the block                 |               |
| Code        | "avgMathOperations"             | average number of math operations per attribute              |               |
| Code        | "maxMathOperations"             | max number of math operations per attribute                  |               |
| Code        | "avgMccabeCC"                   | Maacabe Complexity is measured per attribute, we measure the average of the complexity per attribute in the Block |               |
| Code        | "sumMccabeCC"                   | sum of the complexity per attribute in the Block             |               |
| Code        | "maxMccabeCC"                   | max of the complexity per attribute in the block             |               |
| Code        | "numMetaArg"                    | number of meta-arguments                                     |               |
| Code        | "numObjects"                    | number of objects                                            |               |
| Code        | "maxObjects"                    | max number of objects per attribute                          |               |
| Code        | "avgObjects"                    | average number of objects per attribute                      |               |
| Code        | "numReferences"                 | total number of references (pointers or attribute access) in the block |               |
| Code        | "avgReferences"                 | average number of references per attribute in the block      |               |
| Code        | "maxReferences"                 | max number of references per attribute in the block          |               |
| Code        | "numVars"                       | total number of variable expressions in the block            |               |
| Code        | "avgNumVars"                    | average number of variable expressions per attribute         |               |
| Code        | "maxNumVars"                    | max number of variable expressions per attribute             |               |
| Code        | "numSplatExpressions"           | number of splat expression in the block                      |               |
| Code        | "avgSplatExpressions"           | average number of splat expression per attribute in the block |               |
| Code        | "maxSplatExpressions"           | max number of splat expression per attribute in the block    |               |
| Code        | "numTemplateExpression"         | total number of template expression in the block             |               |
| Code        | "avgTemplateExpression"         | average number of template expression in the block (can be viewed as percentage) |               |
| Code        | "textEntropyMeasure"            | text entropy                                                 |               |
| Code        | "numTokens"                     | number of tokens in the block                                |               |
| Code        | "minTokensPerAttr"              | min number of tokens per attribute                           |               |
| Code        | "maxTokensPerAttr"              | max number of tokens per attribute                           |               |
| Code        | "avgTokensPerAttr"              | average number of tokens per attribute                       |               |
| code        | "numTuples"                     | total number of tuples                                       |               |
| code        | "avgTuples"                     | average number of tuples per Attribute (*can be like percentage if we devide by the attributes number*) |               |
| code        | "maxTuples"                     | max number of tuples per Attribute                           |               |
| code        | "numElemTuples"                 | total number of elements contained in *Tuples*               |               |
| code        | "avgElemTuples"                 | average number of element per *Tuple*                        |               |
| code        | "maxElemTuples"                 | max number of elements per *Tuple*                           |               |
| code        | "depthOfBlock"                  | the size of block [end - start + 1]                          |               |
| code        | "loc"                           | number of lines of code                                      |               |
| code        | "nloc"                          | number of non-lines of code [blank + comments]               |               |
| code        | "isResource"                    | if the block is [*resource*](https://developer.hashicorp.com/terraform/language/resources/syntax) |               |
| code        | "isModule"                      | if the block is [*module*](https://developer.hashicorp.com/terraform/language/modules) |               |
| code        | "isData"                        | if the block is [*Data*](https://developer.hashicorp.com/terraform/language/data-sources) |               |
| code        | "isTerraform"                   | if the block is [*terraform*](https://developer.hashicorp.com/terraform/language/settings) |               |
| code        | "isProvider"                    | if the block is [*provider*](https://developer.hashicorp.com/terraform/language/providers/configuration) |               |
| code        | "isVariable"                    | if the block is [*variable*](https://developer.hashicorp.com/terraform/language/values/variables) |               |
| code        | "isOutput"                      | if the block is [*output*](https://developer.hashicorp.com/terraform/language/values/outputs) |               |
| code        | "isLocals"                      | if the block is [*locals*](https://developer.hashicorp.com/terraform/language/values/locals) |               |
| Code        | "numExplicitResourceDependency" | number of explicit dependency resources                      |               |
| Code        | "avgDepthNestedBlocks"          | average of the depth nested blocks                           |               |
| Code        | "maxDepthNestedBlocks"          | max depth of nested blocks                                   |               |
| Code        | "minDepthNestedBlocks"          | min depth of nested blocks                                   |               |
| Code        | "numDeprecatedFunctions"        | Number of deprecated functions that can lead to bugs such as **list** or **map** |               |
| Code        | "numImplicitDependentResources" | Nuumber of calls to the resource blocks                      |               |
| Code        | "numImplicitDependentData"      | Number of calls to the Data blocks                           |               |
| Code        | "numImplicitDependentModules"   | Number of calls to the Module blocks                         |               |
| Code        | "numImplicitDependentProviders" | Number of calls to the Provider blocks                       |               |
| Code        | "numImplicitDependentLocals"    | Number of callas to the Locals blocks                        |               |
| Code        | "numImplicitDependentVars"      | Number of Calls to the variable blocks                       |               |
| Code        | "numImplicitDependentEach"      | Number of Calls to each meta arguments                       |               |
| Code        | "numEmptyString"                | Number of emtpy string **""** usage. Empty string could lead to defects in some resources as describe in [aws provider upgrade 4](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/guides/version-4-upgrade). |               |
| Code        | "numWildCardSuffixString"       | Number of wild card suffix string                            |               |
| Code        | "numStarString"                 | Number of ***** String usage which can lead to some resource [errors](https://github.com/hashicorp/terraform-provider-aws/issues/10843) . |               |
| Code        | "containDescriptionField"       | Check if a block contains a descritption filed               |               |
| Code        | "numDebuggingFunctions"         | Number of debugging Functions                                |               |
| Code        | "numLookUpFunctionCall"         | Number of calls to *lookup* function                         |               |
| Code        | "numElemObjects"                | Number of elements in the objects                            |               |
| Code        | "avgElemObjects"                | Average Number of element per objects                        |               |
| Code        | "maxElemObjects"                | Max Number of element per objects                            |               |
