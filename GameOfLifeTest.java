import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameOfLifeTest {
	@Test
	public void testConstructorAndGetters() {
		GameOfLife society = new GameOfLife(5, 8);
		assertEquals(5, society.numberOfRows());
		assertEquals(8, society.numberOfColumns());
		for (int r = 0; r < society.numberOfRows(); r++) {
			for (int c = 0; c < society.numberOfColumns(); c++) {
				assertFalse(society.cellAt(r, c));
			}
		}
	}

	@Test
	public void testConstructorAndGettersAllTrue() {
		GameOfLife society = new GameOfLife(3, 3);
		for (int r = 0; r < society.numberOfRows(); r++) {
			for (int c = 0; c < society.numberOfColumns(); c++) {
				society.growCellAt(r, c);
			}
		}
		for (int r = 0; r < society.numberOfRows(); r++) {
			for (int c = 0; c < society.numberOfColumns(); c++) {
				assertTrue(society.cellAt(r, c));
			}
		}
	}

	@Test
	public void testGrowCellAtAndCellAt() {
		GameOfLife society = new GameOfLife(4, 4);
		society.growCellAt(1, 1);
		society.growCellAt(2, 2);
		society.growCellAt(3, 3);
		assertTrue(society.cellAt(1, 1));
		assertTrue(society.cellAt(2, 2));
		assertTrue(society.cellAt(3, 3));
	}

	@Test
	public void testGrowCellAtAndCellAtOneTrue() {
		GameOfLife society = new GameOfLife(4, 4);
		society.growCellAt(1, 1);
		assertTrue(society.cellAt(1, 1));
		assertFalse(society.cellAt(2, 2));
		assertFalse(society.cellAt(3, 3));
	}
	
	@Test
	public void testToStringEmpty() {
		GameOfLife society = new GameOfLife(2, 2);
		String resultStr = society.toString();
		StringBuilder tempBld = new StringBuilder();
		for(int i = 0; i < 2; i++) {
			tempBld.append("..");
			tempBld.append(System.getProperty("line.separator"));
		}
		assertEquals(tempBld.toString(), resultStr);

	}
	
	@Test
	public void testToStringOneCell() {
		GameOfLife society = new GameOfLife(2, 2);
		society.growCellAt(0, 0);
		String resultStr = society.toString();
		StringBuilder tempBld = new StringBuilder();
		for(int i = 0; i < 2; i++) {
			tempBld.append("..");
			tempBld.append(System.getProperty("line.separator"));
		}
		tempBld.replace(0, 1, "O");

		assertEquals(tempBld.toString(), resultStr);

	}

	@Test
	public void testNeighborsNoWrapping() {
		GameOfLife society = new GameOfLife(10, 16);
		society.growCellAt(3, 3);
		society.growCellAt(3, 4);
		society.growCellAt(3, 5);
		assertEquals(0, society.neighborCount(2, 1));
		assertEquals(1, society.neighborCount(2, 2));
		assertEquals(2, society.neighborCount(2, 3));
		assertEquals(3, society.neighborCount(2, 4));
		assertEquals(2, society.neighborCount(2, 5));
		assertEquals(1, society.neighborCount(2, 6));
		assertEquals(0, society.neighborCount(2, 7));

		// Testing next row
		assertEquals(1, society.neighborCount(3, 2));
		// Should not count self
		assertEquals(1, society.neighborCount(3, 3));
		assertEquals(2, society.neighborCount(3, 4));

		// Test the lowest relevant row
		assertEquals(0, society.neighborCount(4, 1));
		assertEquals(1, society.neighborCount(4, 2));
		assertEquals(2, society.neighborCount(4, 3));
		assertEquals(3, society.neighborCount(4, 4));
		assertEquals(2, society.neighborCount(4, 5));
		assertEquals(1, society.neighborCount(4, 6));
		assertEquals(0, society.neighborCount(4, 7));

	}

	@Test
	public void testNeighborsYesWrapping() {
		GameOfLife society = new GameOfLife(5, 5);
		society.growCellAt(0, 0);
		society.growCellAt(0, 1);
		society.growCellAt(1, 0);
		society.growCellAt(1, 1);
		society.growCellAt(4, 0);
		society.growCellAt(4, 1);
		society.growCellAt(0, 4);
		society.growCellAt(1, 4);
		society.growCellAt(4, 4);

		// Top row wrapping to bottom.
		assertEquals(5, society.neighborCount(0, 1));

		// Left wrap to right
		assertEquals(5, society.neighborCount(1, 0));

		// Bottom wrap to top
		assertEquals(3, society.neighborCount(4, 1));

		// Right wrap to left
		assertEquals(3, society.neighborCount(1, 4));

		// Moment of truth!
		assertEquals(8, society.neighborCount(0, 0));

	}

	@Test
	public void testUpdateOneCell() {
		GameOfLife society = new GameOfLife(5, 5);
		society.growCellAt(0, 0);
		assertTrue(society.cellAt(0, 0));
		assertEquals(1, society.neighborCount(0, 1));
		society.update();
		assertFalse(society.cellAt(0, 0));
		assertEquals(0, society.neighborCount(0, 1));

	}

	@Test
	public void testUpdateThreeCellsRepeatPattern() {
		GameOfLife society = new GameOfLife(5, 5);
		society.growCellAt(2, 1);
		society.growCellAt(2, 2);
		society.growCellAt(2, 3);
		assertEquals(3, society.neighborCount(1, 2));
		// System.out.println(society);
		society.update();
		// System.out.println("Updated");
		// System.out.println(society);
		assertTrue(society.cellAt(1, 2));
		assertTrue(society.cellAt(2, 2));
		assertTrue(society.cellAt(3, 2));

		society.update();
		// System.out.println("Updated");
		// System.out.println(society);
		assertTrue(society.cellAt(2, 1));
		assertTrue(society.cellAt(2, 2));
		assertTrue(society.cellAt(2, 3));
	}

	@Test
	public void testUpdateSectionPattern1() {
		GameOfLife society = new GameOfLife(5, 5);
		society.growCellAt(2, 2);
		society.growCellAt(2, 3);
		society.growCellAt(3, 1);
		society.growCellAt(3, 2);
		// System.out.println(society);

		assertEquals(3, society.neighborCount(2, 1));
		assertEquals(3, society.neighborCount(3, 3));

		society.update();
		// System.out.println("Updated");
		// System.out.println(society);

		assertTrue(society.cellAt(2, 1));
		assertTrue(society.cellAt(2, 2));
		assertTrue(society.cellAt(2, 3));
		assertTrue(society.cellAt(3, 1));
		assertTrue(society.cellAt(3, 2));
		assertTrue(society.cellAt(3, 2));
		assertFalse(society.cellAt(1, 1));

	}

	@Test
	public void testUpdateSectionPattern2Wrap() {
		GameOfLife society = new GameOfLife(5, 5);
		society.growCellAt(0, 0);
		society.growCellAt(0, 2);
		society.growCellAt(0, 4);
		society.growCellAt(4, 2);
		society.growCellAt(4, 3);
		society.growCellAt(4, 4);
		// System.out.println(society);

		society.update();
		// System.out.println("updated");
		// System.out.println(society);

		// Check top.
		assertTrue(society.cellAt(0, 0));
		assertTrue(society.cellAt(0, 1));
		assertTrue(society.cellAt(0, 2));
		assertFalse(society.cellAt(0, 3));
		assertTrue(society.cellAt(0, 4));
		assertEquals(5, society.neighborCount(0, 0));
		assertEquals(5, society.neighborCount(0, 1));
		assertEquals(3, society.neighborCount(0, 2));

		// Check mid.
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < 5; j++) {
				assertFalse(society.cellAt(i, j));
			}
		}

		// Check bottom.
		assertTrue(society.cellAt(4, 0));
		assertTrue(society.cellAt(4, 1));
		assertTrue(society.cellAt(4, 2));
		assertFalse(society.cellAt(4, 3));
		assertTrue(society.cellAt(4, 4));
		assertTrue(society.cellAt(3, 3));
		assertEquals(5, society.neighborCount(4, 0));
		assertEquals(5, society.neighborCount(4, 1));
		assertEquals(4, society.neighborCount(4, 2));
		assertEquals(5, society.neighborCount(4, 3));
		assertEquals(4, society.neighborCount(4, 4));

	}

}
