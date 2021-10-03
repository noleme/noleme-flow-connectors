# Noleme Flow Connectors

[![Maven Build](https://github.com/noleme/noleme-flow-connectors/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/noleme/noleme-flow-connectors/actions/workflows/maven.yml)
[![Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-flow-connect-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-flow-connect-parent)
![GitHub](https://img.shields.io/github/license/noleme/noleme-flow-connectors)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fnoleme%2Fnoleme-flow-connectors.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fnoleme%2Fnoleme-flow-connectors?ref=badge_shield)

This library provides utilities for building `noleme-flow` based ETLs.

Implementations found in this package shouldn't be tied to any specific Noleme project.

_Note: This library is considered as "in beta" and as such significant API changes may occur without prior warning._

## I. Installation

_TODO_

## II. Notes on Structure and Design



_TODO_

## III. Usage

Note that sample "toy" programs are also provided: `sample-wordcount` [here](./sample/wordcount).

We'll also write down a basic example of ETL pipeline leveraging some features found in these libraries, we won't touch on the `ETL` classes, these are covered in the sample project.

Most of the syntax is actually from `noleme-flow`, it could be a good idea to start by having a look at it [there](https://github.com/noleme/noleme-flow).

Let us start by imagining we have a tiny CSV dataset like this:

```csv
key,value,metadata
0,234,interesting
1,139,not_interesting
3,982,interesting
```

Here is what a pipeline for manipulating this could look like:

```java
var flow = Flow
    .from(new FileStreamer(), "path/to/my.csv") //We open an inpustream from the CSV file
    .pipe(new TablesawCSVParser()) //We interpret it as CSV and transform it into a tablesaw dataframe
    .pipe(Tablesaw::print) // We print the dataframe to stdout
;

Flow.runAsPipeline(flow);
```

Running the above should display the following, granted a logger configured for printing `INFO` level information:

```log
[main] INFO com.noleme.flow.connect.commons.transformer.filesystem.FileStreamer - Initializing stream from filesystem at data/my.csv
[main] INFO com.noleme.flow.connect.tablesaw.transformer.TablesawCSVParser - Extracting CSV data into dataframe...
[main] INFO com.noleme.flow.connect.tablesaw.transformer.TablesawCSVParser - Extracted 3 lines into dataframe.

 index  |  key  |  value  |     metadata      |
-----------------------------------------------
     0  |    0  |    234  |      interesting  |
     1  |    1  |    139  |  not_interesting  |
     2  |    3  |    982  |      interesting  |
(row_count=3)
```

Note that it added an `index` column, we can remove it by specifying a `TableProperties` object with `setAddRowIndex(false)`.
Let's also add a filter, and a persistence operation:

```java
var tableProperties = new TableProperties().setAddRowIndex(false);

var flow = Flow
    .from(new FileStreamer(), "path/to/my.csv")
    .pipe(new TablesawCSVParser(tableProperties))
    .pipe(Criterion.whereIsEqualTo("metadata", "interesting")) //We use a helper query feature, note that there are many other ways to do that, notably using the tablesaw API
    .sink(new TablesawCSVWrite("path/to/my-filtered.csv")) //We dump the dataframe as CSV into another file
;

Flow.runAsPipeline(flow);
```

Upon running, the above should produce a CSV file like this one:

```csv
key,value,metadata
0,234,interesting
3,982,interesting
```

Will wrap-up this very simple example by replacing the source by one loading the file from AWS:

```java
var tableProperties = new TableProperties().setAddRowIndex(false);

var flow = Flow
    .from(new AmazonS3Streamer(s3, "my-bucket"), "my.csv") // Given a properly configured AmazonS3 instance
    .pipe(new TablesawCSVParser(tableProperties))
    .pipe(Criterion.whereIsEqualTo("metadata", "interesting"))
    .sink(new TablesawCSVWrite("path/to/my-filtered.csv")) // We still write the output to the filesystem
;

Flow.runAsPipeline(flow);
``` 

As the reader can guess, the general idea is to define the execution plan (general structure and type transitions) separately from the choice of implementation used for performing the transformations.
For instance, here, we would likely make the `Extractor` and `Loader` swappable, while retaining the interpretation as a CSV and subsequent filtering.
Some situations may call for entire little pipelines with remote extracting, unzipping, streaming, etc.
The goal was to make it possible to focus on the core logic and retain control over how the pipeline interacts with the outside world.

_TODO_

## IV. Dev Installation

This project will require you to have the following:

* Java 11+
* Git (versioning)
* Maven (dependency resolving, publishing and packaging) 


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fnoleme%2Fnoleme-flow-connectors.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fnoleme%2Fnoleme-flow-connectors?ref=badge_large)
