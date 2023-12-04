<a name="readme-top"></a>

<div align="center">



<h1 align="center">TerraMetrics</h1>

  <p align="center">
    <br />
    <a href="https://www.youtube.com/watch?v=DGeyDlu1fac">View Demo</a>
    ·
    <a href="https://github.com/stilab-ets/terametrics/issues">Report Bug</a>
    ·
    <a href="https://github.com/stilab-ets/terametrics/pulls">Request Feature</a>
  </p>

</div>



<!-- TABLE OF CONTENTS -->

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li><a href="#metrics">Metrics</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#outputs">Outputs</a></li>
    <li><a href="#evaluation">Empirical Validation</a> </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>




<!-- ABOUT THE PROJECT -->

## About The Project

**TerraMetrics** is a quality assurance tool to help Infrastructure-as-Code (IaC) practitioners improving the quality of their 
IaC scripts. This tool is designed to characterize the quality of Terraform artefacts by providing a catalogue of 40 quality metrics 
that could be extracted from a given Hashicorp Configuration Language (HCL) within Terraform (.tf) files. 

The list of these metrics are provided in the next section ([Metrics](#metrics)). These metrics are measured at the block level
within a given file. Each metric can have different aggregation scope (Total, Avg, Max, Min) per block since we extract the metrics information from the attributes containing in a block under analysis.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Metrics

The table below lists the HCL metrics measured by our TerraMetrics. The table contains 100 items, where 5 items represent information concerning the analyzed block (Meta Metric Type), and the remaining 95 items extend our existing 40 metrics by including their aggregations (num == Total, avg, max, min (for some cases)).

| Metric Type | Metric Name                     | Description                                                  |
| ----------- | ------------------------------- | ------------------------------------------------------------ |
| Meta        | "impacted_block_type"           | the block type + block name                                  |
| Meta        | "block"                         | the type of the block                                        |
| Meta        | "start_block"                   | the index start of block in the file                         |
| Meta        | "end_block"                     | the index end of block in the file                           |
| Meta        | "block_identifiers"             | the block identifier concats the block type, block name and its labels |
| Code        | "numComparisonOperators"        | total number of comparison operators in the block            |
| Code        | "avgComparisonOperators"        | average number of comparison operators in the block (can be considered as percentage in the case when we divise by the total number of attributes) |
| Code        | "maxComparisonOperators"        | max number of comparison operators that can be contained in an attribute belonging to the block |
| Code        | "numConditions"                 | total number of conditional expressions in the block         |
| Code        | "numAttrs"                      | total number of attributes in the block                      |
| Code        | "avgConditions"                 | average number of conditional expressions per attribute in the block |
| Code        | "maxConditions"                 | max number of conditional expressions per attribute in the block |
| Code        | "numLogiOpers"                  | total number of logical operators  in the block [**&&, \|\|, !**] |
| Code        | "avgLogiOpers"                  | average number of logical operators per attribute in the block |
| Code        | "maxLogiOpers"                  | max number of logical operators per attribute in the block   |
| Code        | "numDynamicBlocks"              | total number of dynamic blocks in the block                  |
| Code        | "numNestedBlocks"               | total number of nested blocks in the block                   |
| Code        | "numFunctionCall"                | total number of function invocations in the block            |
| Code        | "avgFunctionCall"               | average number of function invocations in the block per attribute |
| Code        | "maxFunctionCall"               | max number of function invocations in the block per attribute |
| Code        | "numParams"                     | total number of parameters in the block contained in the function invocation |
| Code        | "avgParams"                     | average number of parameters per function invocation         |
| Code        | "maxParams"                     | max number of parameters per function invocation             |
| Code        | "numHereDocs"                   | total number of [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) in the block [**<<EOF...EOF**] |
| Code        | "avgHereDocs"                   | average number of [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) in the block per attribute |
| Code        | "numLinesHereDocs"              | total number of lines contained in the [here-doc expressions](https://developer.hashicorp.com/terraform/language/expressions/strings) |
| Code        | "avgLinesHereDocs"              | average number of lines contained in the here-docs expressions |
| Code        | "maxLinesHereDocs"              | max number of lines contained in the here-docs expressions   |
| Code        | "numIndexAccess"                | number of Index Access expressions in the block              |
| Code        | "avgIndexAccess"                | average number of Index Access expressions per attribute in the block |
| Code        | "maxIndexAccess"                | max number of Index Access expressions per attribute in the block |
| Code        | "numLiteralExpression"          | total number of literal expressions in the block             |
| Code        | "numStringValues"               | total number of string values in the block *Hard-Coded*      |
| Code        | "numLoops"                      | total number of loops in the block                           |
| Code        | "avgLoops"                      | average number of loops per attribute (in the block)         |
| Code        | "maxLoops"                      | max number of loops per attribute (in the block)             |
| Code        | "numMathOperations"             | total number of math operations in the block                 |
| Code        | "avgMathOperations"             | average number of math operations per attribute              |
| Code        | "maxMathOperations"             | max number of math operations per attribute                  |
| Code        | "avgMccabeCC"                   | Maacabe Complexity is measured per attribute, we measure the average of the complexity per attribute in the Block |
| Code        | "sumMccabeCC"                   | sum of the complexity per attribute in the Block             |
| Code        | "maxMccabeCC"                   | max of the complexity per attribute in the block             |
| Code        | "numMetaArg"                    | number of meta-arguments                                     |
| Code        | "numObjects"                    | number of objects                                            |
| Code        | "maxObjects"                    | max number of objects per attribute                          |
| Code        | "avgObjects"                    | average number of objects per attribute                      |
| Code        | "numReferences"                 | total number of references (pointers or attribute access) in the block |
| Code        | "avgReferences"                 | average number of references per attribute in the block      |
| Code        | "maxReferences"                 | max number of references per attribute in the block          |
| Code        | "numVars"                       | total number of variable expressions in the block            |
| Code        | "avgNumVars"                    | average number of variable expressions per attribute         |
| Code        | "maxNumVars"                    | max number of variable expressions per attribute             |
| Code        | "numSplatExpressions"           | number of splat expression in the block                      |
| Code        | "avgSplatExpressions"           | average number of splat expression per attribute in the block |
| Code        | "maxSplatExpressions"           | max number of splat expression per attribute in the block    |
| Code        | "numTemplateExpression"         | total number of template expression in the block             |
| Code        | "avgTemplateExpression"         | average number of template expression in the block (can be viewed as percentage) |
| Code        | "textEntropyMeasure"            | text entropy                                                 |
| Code        | "numTokens"                     | number of tokens in the block                                |
| Code        | "minTokensPerAttr"              | min number of tokens per attribute                           |
| Code        | "maxTokensPerAttr"              | max number of tokens per attribute                           |
| Code        | "avgTokensPerAttr"              | average number of tokens per attribute                       |
| code        | "numTuples"                     | total number of tuples                                       |
| code        | "avgTuples"                     | average number of tuples per Attribute (*can be like percentage if we devide by the attributes number*) |
| code        | "maxTuples"                     | max number of tuples per Attribute                           |
| code        | "numElemTuples"                 | total number of elements contained in *Tuples*               |
| code        | "avgElemTuples"                 | average number of element per *Tuple*                        |
| code        | "maxElemTuples"                 | max number of elements per *Tuple*                           |
| code        | "depthOfBlock"                  | the size of block [end - start + 1]                          |
| code        | "loc"                           | number of lines of code                                      |
| code        | "nloc"                          | number of non-lines of code [blank + comments]               |
| code        | "isResource"                    | if the block is [*resource*](https://developer.hashicorp.com/terraform/language/resources/syntax) |
| code        | "isModule"                      | if the block is [*module*](https://developer.hashicorp.com/terraform/language/modules) |
| code        | "isData"                        | if the block is [*Data*](https://developer.hashicorp.com/terraform/language/data-sources) |
| code        | "isTerraform"                   | if the block is [*terraform*](https://developer.hashicorp.com/terraform/language/settings) |
| code        | "isProvider"                    | if the block is [*provider*](https://developer.hashicorp.com/terraform/language/providers/configuration) |
| code        | "isVariable"                    | if the block is [*variable*](https://developer.hashicorp.com/terraform/language/values/variables) |
| code        | "isOutput"                      | if the block is [*output*](https://developer.hashicorp.com/terraform/language/values/outputs) |
| code        | "isLocals"                      | if the block is [*locals*](https://developer.hashicorp.com/terraform/language/values/locals) |
| Code        | "numExplicitResourceDependency" | number of explicit dependency resources                      |
| Code        | "avgDepthNestedBlocks"          | average of the depth nested blocks                           |
| Code        | "maxDepthNestedBlocks"          | max depth of nested blocks                                   |
| Code        | "minDepthNestedBlocks"          | min depth of nested blocks                                   |
| Code        | "numDeprecatedFunctions"        | Number of deprecated functions that can lead to bugs such as **list** or **map** |
| Code        | "numImplicitDependentResources" | Nuumber of calls to the resource blocks                      |
| Code        | "numImplicitDependentData"      | Number of calls to the Data blocks                           |
| Code        | "numImplicitDependentModules"   | Number of calls to the Module blocks                         |
| Code        | "numImplicitDependentProviders" | Number of calls to the Provider blocks                       |
| Code        | "numImplicitDependentLocals"    | Number of callas to the Locals blocks                        |
| Code        | "numImplicitDependentVars"      | Number of Calls to the variable blocks                       |
| Code        | "numImplicitDependentEach"      | Number of Calls to each meta arguments                       |
| Code        | "numEmptyString"                | Number of emtpy string **""** usage. Empty string could lead to defects in some resources as describe in [aws provider upgrade 4](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/guides/version-4-upgrade). |
| Code        | "numWildCardSuffixString"       | Number of wild card suffix string                            |
| Code        | "numStarString"                 | Number of ***** String usage which can lead to some resource [errors](https://github.com/hashicorp/terraform-provider-aws/issues/10843) . |
| Code        | "containDescriptionField"       | Check if a block contains a descritption filed               |
| Code        | "numDebuggingFunctions"         | Number of debugging Functions                                |
| Code        | "numLookUpFunctionCall"         | Number of calls to *lookup* function                         |
| Code        | "numElemObjects"                | Number of elements in the objects                            |
| Code        | "avgElemObjects"                | Average Number of element per objects                        |
| Code        | "maxElemObjects"                | Max Number of element per objects                            |



<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->

## Getting Started

To get a local copy of **TerraMetrics** up and running follow these steps.

### Prerequisites

Before you begin, make sure you have the following installed:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) (version 11.0)

- [Maven](https://maven.apache.org/download.cgi) (version 3.7+)




### Installation

1. Clone the repository to your local machine:

    ```bash
    git clone https://github.com/stilab-ets/terametrics.git
    ```

2. Navigate to the project directory:

    ```bash
    cd terametrics
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```


<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->

## Usage 

**TerraMetrics** is available as JAR file that could be standalone executable file. The use can find the JAR file named as [terraform_metrics-1.0.jar](src/main/resources/terraform_metrics-1.0.jar).

To run the tool you need execute [terraform_metrics-1.0.jar](src/main/resources/terraform_metrics-1.0.jar) with the right parameters. In the following we detail the possible parameters:

1. To view the documentation:

    - ```-h```: boolean parameter to print the user guide.
   
    ```bash
    java -jar /path/to/terraform_metrics-1.0.jar -h
    ```

2. To measure the HCL metrics for a given Terraform file, 3 parameters are required:
  
   -  ```--file```: path to the Terraform file.
   -  ```-b```: boolean parameter to indicate the file-level execution.
   -  ```--target```: path to the result file.
   
   ```bash
        java -jar </path/to/terraform_metrics-1.0.jar> --file </path/to/file.tf> -b --target </path/to/target.json>
   ```

3. To measure the HCL metrics for a given *Local Folder* or *GitHub Repository* that contain Terraform file, 4 parameters are required:

   -  ```-l```: boolean parameter to indicate the local-level execution.
   -  ```--repo```: to indicate the path to the local folder.
   - ```--target```: to indicate the path to the output file.
   - ```--project```: to indicate the project name.
   - ```-g```: boolean parameter to indicate to get the repository from GitHub.

   For *Local Folder*: 

   ```bash
      java -jar /path/to/terraform_metrics-1.0.jar -l --repo </path/to/localrepo> --target </path/to/target.json> --project <projectName>
   ```

   For *GitHub Repository*:

   ```bash
      java -jar /path/to/terraform_metrics-1.0.jar -g --repo </path/to/localrepo> --target </path/to/target.json> --project <GitHubRepositoryFullName>
   ```

A demonstration video is available at:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/DGeyDlu1fac/0.jpg)](https://www.youtube.com/watch?v=DGeyDlu1fac)


## Outputs

In the following, we provide such cases of JSON file output generated by **TerraMetrics**:

1. When the execution concerns a single File:

    ```
    {
              "head": {
              "num_lines_of_code": 133,
              "num_providers": 0,
              "num_outputs": 0,
              "num_terraform": 0,
              "num_resources": 1,
              "num_data": 0,
              "num_blocks": 1,
              "num_locals": 0,
              "num_variables": 0,
              "num_modules": 0
              },
              "data": [
                  {
                      "numLinesHereDocs": 89,
                      "minAttrsTextEntropy": 2.85,
                      "avgFunctionCall": 0.39,
                      "avgLoops": 0.13,
                      "avgMccabeCC": 1.22,
                      "maxLoops": 2,
                      "maxAttrsTextEntropy": 5.48,
                      "numDeprecatedFunctions": 0,
                      "avgReferences": 1.48,
                      "avgIndexAccess": 0.09,
                      "numImplicitDependentEach": 2,
                      "avgLogiOpers": 0.22,
                      "block": "resource",
                      ...........
                  }
              ],
              "status": 200
    }
    ```


2. When the execution concerns a local or GitHub repository, the file generated has this format:

    ```
    {
        "data": [
            {
                "file": "C:\\...\\...\\...\\test\\local-module-tf\\data-management.tf",
                "blocks": [
                    {
                        "numLinesHereDocs": 0,
                        "minAttrsTextEntropy": 3.42,
                        "avgFunctionCall": 0.0,
                        "avgLoops": 0.0,
                        "avgMccabeCC": 1.0,
                        "maxLoops": 0,
                        "maxAttrsTextEntropy": 3.76,
                        "numDeprecatedFunctions": 0,
                        "avgReferences": 0.67,
                        ......
                    }
                .......
                ]
            }
            .....
        ],
        "project": "test-project"
    }
    ```
  

[//]: # (Please refer to the [Video]&#40;https://youtu.be/386prRYfLIk&#41; that presents a demonstration how to use this package.)

## Evaluation

The evaluation of the usefulness of TerraMetrics is available in an external package 
called [TerraMetrics: An Open Source Tool for Infrastructure-as-Code (IaC) Quality Metrics in Terraform](https://figshare.com/s/325c9bd2205ca644da41) 
in figshare that contains a more detailed overview of how the author papers performed their evaluation.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->

## Roadmap

- [ ] Add a Docker File
- [ ] Add Continuous Integration Pipeline
- [ ] Test Deprecation Visitors

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Your contributions are **genuinely valued** and **greatly appreciated**.

If you have ideas or improvements to enhance the project, we encourage you to fork the repository and initiate a pull request. Alternatively, feel free to open an issue labeled "enhancement" to share your suggestions. Don't forget to show your support by starring the project! Thank you once again for being a part of this collaborative journey.

Here's a step-by-step guide to guide you through the contribution process:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->

## Contact

Mahi BEGOUG - mahi.begoug.rch[at]gmail.com


<p align="right">(<a href="#readme-top">back to top</a>)</p>

