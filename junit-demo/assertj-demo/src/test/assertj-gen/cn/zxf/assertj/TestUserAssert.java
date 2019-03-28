package cn.zxf.assertj;

/**
 * {@link TestUser} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractTestUserAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class TestUserAssert extends AbstractTestUserAssert<TestUserAssert, TestUser> {

  /**
   * Creates a new <code>{@link TestUserAssert}</code> to make assertions on actual TestUser.
   * @param actual the TestUser we want to make assertions on.
   */
  public TestUserAssert(TestUser actual) {
    super(actual, TestUserAssert.class);
  }

  /**
   * An entry point for TestUserAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myTestUser)</code> and get specific assertion with code completion.
   * @param actual the TestUser we want to make assertions on.
   * @return a new <code>{@link TestUserAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static TestUserAssert assertThat(TestUser actual) {
    return new TestUserAssert(actual);
  }
}