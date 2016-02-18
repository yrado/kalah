package yurius.game.controller;

import java.util.Arrays;

class IntFactory
{
  final private int size;

  public IntFactory(int size)
  {
    this.size = size;
  }

  public int[] getInts(int defaultValue, int... positionValuesPairs)
  {
    int[] result = new int[size];
    Arrays.fill(result,  defaultValue);

    if (positionValuesPairs.length % 2 != 0)
      throw new IllegalArgumentException("positionValuesPairs must have odd size");

    for (int i = 0; i < positionValuesPairs.length; i += 2)
      result[positionValuesPairs[i]] = positionValuesPairs[i + 1];
    return result;
  }
}
