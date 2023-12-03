<a name="readme-top"></a>

<div align="center">



<h1 align="center">TerraMetrics</h1>

  <p align="center">
    <br />
    <a href="https://www.youtube.com/">View Demo</a>
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
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
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

The list of these metrics are provided in [BLOCK_METRICS.md](BLOCK_METRICS.md) file. These metrics are measured at the block level
within a given file. Each metric can have different aggregation scope (Total, Avg, Max, Min) per block since we extract the metrics information from the attributes containing in a block under analysis.

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

Please refer to the [Video](https://youtu.be/386prRYfLIk) that presents a demonstration how to use this package.

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

