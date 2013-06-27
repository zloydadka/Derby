package main.java.com.github.model;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.java.com.github.view.ViewContext;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractModel {

	@DatabaseField(generatedId = true)
	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private static final Map<DataType, Class<? extends Object>> fieldTypes = new HashMap<DataType, Class<? extends Object>>();
	static {

		fieldTypes.put(DataType.STRING, String.class);
		fieldTypes.put(DataType.BOOLEAN_OBJ, Boolean.class);
		fieldTypes.put(DataType.INTEGER_OBJ, Integer.class);
		fieldTypes.put(DataType.DATE, Date.class);

	}

	protected Set<Entry<String, Field>> getFieldsByModel() {

		Class<?> clazz = this.getClass();
		HashMap<String, Field> hashMap = new HashMap<String, Field>();
		while (AbstractModel.class.isAssignableFrom(clazz)) {
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				String fieldName = field.getName();

				if (!hashMap.containsKey(fieldName)
						&& field.getAnnotation(DatabaseField.class) != null) {
					hashMap.put(fieldName, field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return hashMap.entrySet();

	}

	public ViewContext getViewContext() {

		ViewContext data = new ViewContext();

		for (Entry<String, Field> entry : this.getFieldsByModel()) {
			Field field = entry.getValue();
			field.setAccessible(true);
			try {
				if (field.getType().equals(Date.class)) {
					SimpleDateFormat format = new SimpleDateFormat(
							"dd.MM.yyyy HH:mm:SS");
					data.put(field.getName(), format.format(field.get(this)));
				} else {
					data.put(field.getName(), field.get(this));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return data;

	}

	public ViewContext emptyContext() {

		ViewContext data = new ViewContext();

		for (Entry<String, Field> entry : this.getFieldsByModel()) {
			Field field = entry.getValue();
			field.setAccessible(true);
			DatabaseField annotation = field.getAnnotation(DatabaseField.class);
			if (annotation != null) {
				try {
					DataType dataType = annotation.dataType();
					if (!dataType.equals(DataType.UNKNOWN)) {
						if (dataType.equals(DataType.DATE)) {
							SimpleDateFormat format = new SimpleDateFormat(
									"dd.MM.yyyy HH:mm:SS");
							data.put(field.getName(), format.format(new Date()));
						} else {
							data.put(field.getName(), fieldTypes.get(dataType)
									.newInstance());
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return data;

	}

	public AbstractModel update(ViewContext data) {

		for (Entry<String, Field> entry : this.getFieldsByModel()) {

			Field field = entry.getValue();
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = data.get(fieldName);
			if (value == null) {
				continue;
			}
			try {
				Type type = field.getType();

				if (type.equals(String.class)) {
					field.set(this, String.valueOf(value));
				} else if (type.equals(Integer.class)) {
					field.set(this, Integer.valueOf(String.valueOf(value)));
				} else if (type.equals(Boolean.class)) {
					field.set(this, Boolean.valueOf(String.valueOf(value)));
				} else if (type.equals(Date.class)) {
					SimpleDateFormat format = new SimpleDateFormat(
							"dd.MM.yyyy HH:mm:SS");
					field.set(this, format.parse(value.toString()));
				}

			} catch (IllegalArgumentException | IllegalAccessException
					| ParseException e) {
				e.printStackTrace();
			}
		}

		return this;

	}
}
