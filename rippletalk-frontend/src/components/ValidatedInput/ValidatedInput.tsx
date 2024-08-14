import React, { useEffect, useState } from 'react';

import { ValidatedInputState } from '../../utils/GlobalInterfaces';

import { determineValidatedStyles } from '../../utils/DetermineStylesUtils';

import { StyledInputBox, StyledInputLabel } from './StyledInput';

import './ValidatedInput.css';

interface ValidatedUserInputProps {
	name: string;
	label: string;
	errorMessage: string;
	validator(value: string): boolean;
	changeValue(e: React.ChangeEvent<HTMLInputElement>): void;
	attributes?: Record<string, string | number | boolean>;
}

export const ValidatedInput: React.FC<ValidatedUserInputProps> = ({
	name,
	label,
	errorMessage,
	validator,
	changeValue,
	attributes,
}) => {
	const [validatedState, setValidatedState] = useState<ValidatedInputState>({
		active: false,
		valid: true,
		typedIn: false,
		labelActive: false,
		labelColor: 'gray',
		value: '',
	});

	const focus = (e: React.FocusEvent<HTMLInputElement>): void => {
		setValidatedState({
			...validatedState,
			active: !validatedState?.active,
		});
	};

	const updateValue = (e: React.ChangeEvent<HTMLInputElement>): void => {
		setValidatedState({
			...validatedState,
			typedIn: true,
			value: e.target.value,
		});

		changeValue(e);
	};

	useEffect(() => {
		setValidatedState(determineValidatedStyles(validatedState, validator));
	}, [
		validatedState.active,
		validatedState.typedIn,
		validatedState.value,
		validatedState.labelActive,
		validatedState.labelColor,
	]);

	return (
		<div className='validated-input'>
			<StyledInputBox
				active={validatedState.active}
				valid={validatedState.valid}
			>
				<StyledInputLabel
					color={validatedState.labelColor}
					active={validatedState.labelActive}
					valid={validatedState.valid}
				>
					{label}
				</StyledInputLabel>
				<input
					className='validated-input-value'
					onFocus={focus}
					onBlur={focus}
					onChange={updateValue}
					{...attributes}
				></input>
			</StyledInputBox>
			{validatedState.valid ? <></> : <span>{errorMessage}</span>}
		</div>
	);
};
