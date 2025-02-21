## `Logger` tag should be determined by the following conditions:

- if `Logger` is called from a top-level function, the tag is `"{FunctionName}"`.
- if `Logger` is called from a getter or setter of any class, the tag is
  `"{ClassName}::{PropertyName}"`.
- if `Logger` is called from a function of an implementor of `Platform`, the tag is
  `"{FunctionName}"`.
- if `Logger` is called from the function `onCreate` of an implementor of `ComponentActivity()`,
  the tag is `"{ClassName}".`
- if `Logger` is called from a function of any other class not mentioned, the tag is
  `"{ClassName}::{FunctionName}"`.
