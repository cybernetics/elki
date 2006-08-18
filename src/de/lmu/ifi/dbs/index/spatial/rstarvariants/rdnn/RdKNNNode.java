package de.lmu.ifi.dbs.index.spatial.rstarvariants.rdnn;

import de.lmu.ifi.dbs.distance.NumberDistance;
import de.lmu.ifi.dbs.index.spatial.rstarvariants.AbstractRStarTreeNode;
import de.lmu.ifi.dbs.persistent.PageFile;
import de.lmu.ifi.dbs.utilities.Util;

/**
 * Represents a node in a RDkNN-Tree.
 *
 * @author Elke Achtert (<a
 *         href="mailto:achtert@dbs.ifi.lmu.de">achtert@dbs.ifi.lmu.de</a>)
 */
public class RdKNNNode<D extends NumberDistance<D>> extends AbstractRStarTreeNode<RdKNNNode<D>, RdKNNEntry<D>> {
  /**
   * Empty constructor for Externalizable interface.
   */
  public RdKNNNode() {
  }

  /**
   * Creates a new RdKNNNode object.
   *
   * @param file     the file storing the RdKNN-Tree
   * @param capacity the capacity (maximum number of entries plus 1 for overflow)
   *                 of this node
   * @param isLeaf   indicates wether this node is a leaf node
   */
  public RdKNNNode(PageFile<RdKNNNode<D>> file, int capacity, boolean isLeaf) {
    super(file, capacity, isLeaf);
  }

  /**
   * Computes and returns the aggregated knn distance of this node
   *
   * @return the aggregated knn distance of this node
   */
  protected D kNNDistance() {
    D result = getEntry(0).getKnnDistance();
    for (int i = 1; i < getNumEntries(); i++) {
      D knnDistance = getEntry(i).getKnnDistance();
      result = Util.max(result, knnDistance);
    }
    return result;
  }

  /**
   * Creates a new leaf node with the specified capacity.
   *
   * @param capacity the capacity of the new node
   * @return a new leaf node
   */
  protected RdKNNNode<D> createNewLeafNode(int capacity) {
    return new RdKNNNode<D>(getFile(), capacity, true);
  }

  /**
   * Creates a new directory node with the specified capacity.
   *
   * @param capacity the capacity of the new node
   * @return a new directory node
   */
  protected RdKNNNode<D> createNewDirectoryNode(int capacity) {
    return new RdKNNNode<D>(getFile(), capacity, false);
  }

  /**
   * Adjusts the parameters of the entry representing the specified node.
   *
   * @param node  the node
   * @param index the index of the entry representing the node in this node's entries array
   */
  public void adjustEntry(RdKNNNode<D> node, int index) {
    RdKNNEntry<D> entry = getEntry(index);
    entry.setKnnDistance(node.kNNDistance());
  }

}
