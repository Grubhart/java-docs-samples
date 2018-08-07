/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vision;

// [START product_search_import]
import com.google.cloud.vision.v1p3beta1.CreateProductSetRequest;
import com.google.cloud.vision.v1p3beta1.LocationName;
import com.google.cloud.vision.v1p3beta1.Product;
import com.google.cloud.vision.v1p3beta1.ProductName;
import com.google.cloud.vision.v1p3beta1.ProductSearchClient;
import com.google.cloud.vision.v1p3beta1.ProductSet;
import com.google.cloud.vision.v1p3beta1.ProductSetName;

import java.io.IOException;
import java.io.PrintStream;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
// [END product_search_import]

/**
 * This application demonstrates how to perform basic operations on Product Sets.
 *
 * For more information, see the tutorial page at
 * https://cloud.google.com/vision/product-search/docs/
 */

public class ProductSetManagement {

  // [START product_search_create_product_set]
  /**
   * Create a product set
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @param productSetId - Id of the product set.
   * @param productSetDisplayName - Display name of the product set.
   * @throws IOException - on I/O errors.
   */
  public static void createProductSet(
      String projectId, String computeRegion, String productSetId, String productSetDisplayName)
      throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // A resource that represents Google Cloud Platform location.
    LocationName projectLocation = LocationName.of(projectId, computeRegion);

    // Create a product set with the product set specification in the region.
    ProductSet myProductSet = ProductSet.newBuilder().setDisplayName(productSetDisplayName).build();
    CreateProductSetRequest request =
        CreateProductSetRequest.newBuilder()
            .setParent(projectLocation.toString())
            .setProductSet(myProductSet)
            .setProductSetId(productSetId)
            .build();
    ProductSet productSet = client.createProductSet(request);

    // Display the product set information
    System.out.println(String.format("Product set name: %s", productSet.getName()));
  }
  // [END product_search_create_product_set]

  // [START product_search_list_product_sets]
  /**
   * List all product sets
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @throws IOException - on I/O errors.
   */
  public static void listProductSets(String projectId, String computeRegion) throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // A resource that represents Google Cloud Platform location.
    LocationName projectLocation = LocationName.of(projectId, computeRegion);

    // List all the product sets available in the region.
    for (ProductSet productSet : client.listProductSets(projectLocation).iterateAll()) {
      // Display the product set information
      System.out.println(String.format("Product set name: %s", productSet.getName()));
      System.out.println(
          String.format(
              "Product set id: %s",
              productSet.getName().substring(productSet.getName().lastIndexOf('/') + 1)));
      System.out.println(
          String.format("Product set display name: %s", productSet.getDisplayName()));
      System.out.println("Product set index time:");
      System.out.println(String.format("\tseconds: %s", productSet.getIndexTime().getSeconds()));
      System.out.println(String.format("\tnanos: %s", productSet.getIndexTime().getNanos()));
    }
  }
  // [END product_search_list_product_sets]

  // [START product_search_get_product_set]
  /**
   * Get info about the product set.
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @param productSetId - Id of the product set.
   * @throws IOException - on I/O errors.
   */
  public static void getProductSet(String projectId, String computeRegion, String productSetId)
      throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // Get the full path of the product set.
    ProductSetName productSetPath = ProductSetName.of(projectId, computeRegion, productSetId);

    // Get complete detail of the product set.
    ProductSet productSet = client.getProductSet(productSetPath);

    // Display the product set information
    System.out.println(String.format("Product set name: %s", productSet.getName()));
    System.out.println(
        String.format(
            "Product set id: %s",
            productSet.getName().substring(productSet.getName().lastIndexOf('/') + 1)));
    System.out.println(String.format("Product set display name: %s", productSet.getDisplayName()));
    System.out.println("Product set index time:");
    System.out.println(String.format("\tseconds: %s", productSet.getIndexTime().getSeconds()));
    System.out.println(String.format("\tnanos: %s", productSet.getIndexTime().getNanos()));
  }
  // [END product_search_get_product_set]

  // [START product_search_list_products_in_product_set]
  /**
   * List all products in a product set.
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @param productSetId - Id of the product set.
   * @throws IOException - on I/O errors.
   */
  public static void listProductsInSet(String projectId, String computeRegion, String productSetId)
      throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // Get the full path of the product set.
    ProductSetName productSetPath = ProductSetName.of(projectId, computeRegion, productSetId);

    // List all the products available in the product set.
    for (Product product :
        client.listProductsInProductSet(productSetPath.toString()).iterateAll()) {
      // Display the product information
      System.out.println(String.format("Product name: %s", product.getName()));
      System.out.println(
          String.format(
              "Product id: %s",
              product.getName().substring(product.getName().lastIndexOf('/') + 1)));
      System.out.println(String.format("Product display name: %s", product.getDisplayName()));
      System.out.println(String.format("Product description: %s", product.getDescription()));
      System.out.println(String.format("Product category: %s", product.getProductCategory()));
      System.out.println(
          String.format("Product labels: %s\n", product.getProductLabelsList().toString()));
    }
  }
  // [END product_search_list_products_in_product_set]

  // [START product_search_add_product_to_product_set]
  /**
   * Add a product to a product set.
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @param productId - Id of the product.
   * @param productSetId - Id of the product set.
   * @throws IOException - on I/O errors.
   */
  public static void addProductToSet(
      String projectId, String computeRegion, String productId, String productSetId)
      throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // Get the full path of the product set.
    ProductSetName productSetPath = ProductSetName.of(projectId, computeRegion, productSetId);

    // Get the full path of the product.
    String productPath = ProductName.of(projectId, computeRegion, productId).toString();

    // Add the product to the product set.
    client.addProductToProductSet(productSetPath, productPath);

    System.out.println(String.format("Product added to product set."));
  }
  // [END product_search_add_product_to_product_set]

  // [START product_search_delete_product_set]
  /**
   * Delete a product set.
   *
   * @param projectId - Id of the project.
   * @param computeRegion - Region name.
   * @param productSetId - Id of the product set.
   * @throws IOException - on I/O errors.
   */
  public static void deleteProductSet(
      String projectId, String computeRegion, String productSetId) throws IOException {
    ProductSearchClient client = ProductSearchClient.create();

    // Get the full path of the product set.
    ProductSetName productSetPath = ProductSetName.of(projectId, computeRegion, productSetId);

    // Delete the product set.
    client.deleteProductSet(productSetPath.toString());

    System.out.println(String.format("Product set deleted"));
  }
  // [END product_search_delete_product_set]

  public static void main(String[] args) throws Exception {
    ProductSetManagement productSetManagement = new ProductSetManagement();
    productSetManagement.argsHelper(args, System.out);
  }

  public static void argsHelper(String[] args, PrintStream out) throws Exception {
    ArgumentParser parser = ArgumentParsers.newFor("Product Set Management").build();
    Subparsers subparsers = parser.addSubparsers().dest("command");

    Subparser createProductSetParser = subparsers.addParser("create_product_set");
    createProductSetParser.addArgument("productSetId");
    createProductSetParser.addArgument("productSetDisplayName");

    Subparser addProductParser = subparsers.addParser("add_product_to_product_set");
    addProductParser.addArgument("productSetId");
    addProductParser.addArgument("productId");

    subparsers.addParser("list_product_sets");

    Subparser getProductSetParser = subparsers.addParser("get_product_set");
    getProductSetParser.addArgument("productSetId");

    Subparser listProductsInProductSetParser = subparsers.addParser("list_products_in_product_set");
    listProductsInProductSetParser.addArgument("productSetId");

    Subparser deleteProductSetParser = subparsers.addParser("delete_product_set");
    deleteProductSetParser.addArgument("productSetId");

    String projectId = System.getenv("PROJECT_ID");
    String computeRegion = System.getenv("REGION_NAME");

    Namespace ns = null;
    try {
      ns = parser.parseArgs(args);
      if (ns.get("command").equals("create_product_set")) {
        createProductSet(
            projectId,
            computeRegion,
            ns.getString("productSetId"),
            ns.getString("productSetDisplayName"));
      }
      if (ns.get("command").equals("list_product_sets")) {
        listProductSets(projectId, computeRegion);
      }
      if (ns.get("command").equals("get_product_set")) {
        getProductSet(projectId, computeRegion, ns.getString("productSetId"));
      }
      if (ns.get("command").equals("add_product_to_product_set")) {
        addProductToSet(
            projectId, computeRegion, ns.getString("productId"), ns.getString("productSetId"));
      }
      if (ns.get("command").equals("list_products_in_product_set")) {
        listProductsInSet(projectId, computeRegion, ns.getString("productSetId"));
      }
      if (ns.get("command").equals("delete_product_set")) {
        deleteProductSet(projectId, computeRegion, ns.getString("productSetId"));
      }

    } catch (ArgumentParserException e) {
      parser.handleError(e);
    }
  }
}