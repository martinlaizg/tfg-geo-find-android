package com.martinlaizg.geofind.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SecureTest {

	private String input;
	private String expected;

	public SecureTest(String input, String expected) {
		this.input = input;
		this.expected = expected;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{//
				{"martinlaizg",
						"cd17168a0bb539e433ad3d527067defd5f5c1588f12fdf420da330d17a303b3e8ebaac6ea1729a4e4089c51517b6b0ef6899ea32b1eaf5d998b7e86ab8b4e8c9"},
				{"contraseña\n" + "con\n" + "saltos\n" + "de\n" + "linea",
						"03d555c1800a811b7e156f5b9add0320f85d6b4b9af07809867292ff442546cb5ab6ce6257cdf4d406756d09afbc402032f86e9619ba24930d541754b9be3d91"},
				{"contraseña con espacios",
						"473e41e2386381dd5ee340ed53ee25d3e5f8860d9ac94bfff96283e1e3633fd9723935cb520941e32a2ad523b85390bbd8291cf8756c7ca6abbc7ad92f0e6b89"},
				{"contraseña con espacios\ny saltos\nde linea",
						"fe59bbcf535f18850977c351494bf93217e1d4c021703533df8ba47e7d51effff91319835b545f0394708580246ec7be15784353c2a16442ef7ca4fb92a45cab"},
				{"&2\"+\"##",
						"4f350a9ca22f67acd2d806e38ec3f5d2b82aae271a6f48881cfd28d31b51267b7e96a35eff1561d7be3beabe2c3b1429e8d21f123196f093f79595fa466f0234"},
				{"!](={)$,,(\"{*]!!",
						"53140b0e62a4493a35bb0214c0070189bbfd920243bbc87a452c42330490d0e42df75052b4f656bb700e08dcd1340478b7ce83faaf95d35a817e08d699d9cd5c"}});
	}

	@Test
	public void hashTest() {
		assertEquals(expected, Secure.hash(input));
	}

}