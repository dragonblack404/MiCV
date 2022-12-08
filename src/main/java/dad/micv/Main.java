package dad.micv;

public class Main {
	
//	private static Gson gson = FxGson.fullBuilder()
//			.setPrettyPrinting()
//			.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//			.create();
//	
//	public static void main(String[] args) throws IOException {
//		
// 
// 
//		List<Integer> numeros = Arrays.asList(6, 8, 34, 25, 32);
//		List<Integer> cuadradosMayores1000 = numeros
//				.stream()
//				.filter(n -> (n * n) >= 1000)
//				.collect(Collectors.toList());
// 
//		System.out.println(cuadradosMayores1000);
// 
// 
//	}
//
//	private static void loadCV() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
//		
//		File cvFile = new File("cv.json"); 
//		
//		CV cv = gson.fromJson(new FileReader(cvFile), CV.class);
//		
//		System.out.println(cv.getPersonal().getNombre() + " " + cv.getPersonal().getApellidos());
//		
//	}
//
//	private static void saveCV() throws IOException {
//		Personal personal = new Personal();
//		personal.setIdentificacion("12345678Z");
//		personal.setNombre("Chuck");
//		personal.setApellidos("Norris");
//		personal.getNacionalidades().add(new Nacionalidad("Española"));
//		personal.getNacionalidades().add(new Nacionalidad("Americana"));
//		personal.setPais("Estados Unidos");
//		personal.setFechaNacimiento(LocalDate.of(1954, 11, 25));
//		
//		CV cv = new CV();
//		cv.setPersonal(personal);
//		
//		String json = gson.toJson(cv, CV.class);
//		
//		File cvFile = new File("cv.json"); 
//		
//		Files.writeString(cvFile.toPath(), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
//	}
	
	public static void main(String[] args) {
		App.main(args);
	}

}
