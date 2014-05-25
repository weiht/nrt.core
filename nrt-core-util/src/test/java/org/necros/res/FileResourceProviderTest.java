package org.necros.res;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.necros.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
public class FileResourceProviderTest {
	@Value("${repo.dirs}")
	private String repoDirs;
	@Value("${repo.base}")
	private String repoBase;
	@Resource(name="fileResourceProvider")
	private ResourceProvider provider;
	
	private void cleanRepo() {
		removeFs(new File(repoBase));
	}

	private void removeFs(File fs) {
		if (fs.isDirectory()) {
			for (File f: fs.listFiles()) {
				removeFs(f);
			}
		}
		fs.delete();
	}
	
	private void initRepo() throws IOException {
		File rd, r;
		r = mkRepoDir("repodira");
		//Repo a
		rd = mkRepo(r, "repoa", true);
		crRes(rd, null, "index.html");
		crRes(rd, "/src/main/groovy", "index.groovy");
		crRes(rd, "/src/main/resources", "static/index.js");
		//Repo b
		rd = mkRepo(r, "repob", false);
		crRes(rd, null, "default.html");
		crRes(rd, "/src/main/resources", "default.py");
		crRes(rd, "/src/main/resources", "static/default.js");
		//Repo c, not searched.
		rd = new File(r, "repoc");
		rd.mkdirs();
		crRes(rd, null, "home.html");
		r = mkRepoDir("repodirb");
		//Another repo a
		rd = mkRepo(r, "repoa", true);
		crRes(rd, null, "root.html");
		crRes(rd, "/src/main/groovy", "root/root.groovy");
		crRes(rd, "/src/main/resources", "/static/root/root.js");
	}
	
	private File mkRepoDir(String n) {
		File r = new File(repoBase, n);
		if (!r.exists()) {
			r.mkdirs();
		}
		return r;
	}

	private File mkRepo(File r, String rn, boolean git) throws IOException {
		File rd = new File(r, rn);
		rd.mkdirs();
		if (git) {
			File g = new File(rd, ".git");
			g.mkdir();
		} else {
			File m = new File(rd, "pom.xml");
			m.createNewFile();
		}
		return rd;
	}

	private File crRes(File dir, String path, String name) throws IOException {
		File p = dir;
		if (path != null) {
			p = new File(p, path);
			if (!p.exists()) {
				p.mkdirs();
			}
		}
		File f = new File(p, name);
		p = f.getParentFile();
		if (!p.exists()) {
			p.mkdirs();
		}
		f.createNewFile();
		return f;
	}

	@Before
	public void setUp() throws Exception {
		cleanRepo();
		initRepo();
	}

	@After
	public void tearDown() throws Exception {
		cleanRepo();
	}

	@Test
	public void testRead() {
		// Repo a
		resourceExistance("index.html", true);
		resourceExistance("index.groovy", true);
		resourceExistance("index.js", false);
		resourceExistance("static/index.js", true);
		// Repo b
		resourceExistance("default.html", true);
		resourceExistance("default.py", true);
		resourceExistance("default.js", false);
		resourceExistance("static/default.js", true);
		// Repo c
		resourceExistance("home.html", false);
		// Repo a'
		resourceExistance("root.html", true);
		resourceExistance("root/root.groovy", true);
		resourceExistance("/static/root/root.js", true);
	}
	
	private void resourceExistance(String name, boolean exists) {
		InputStream ins;
		ins = provider.read(name);
		if (exists) {
			assertNotNull(name, ins);
		} else {
			assertNull(name, ins);
		}
		ResourceUtils.closeStream(ins);
	}
}
